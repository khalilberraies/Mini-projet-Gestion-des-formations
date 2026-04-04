import React, { useState, useEffect, useCallback } from 'react';
import api from '../api/api';

const EMPTY = { nom:'', prenom:'', email:'', tel:'', idStructure:'', idProfil:'' };

function validate(f) {
  const e = {};
  if (!f.nom.trim())    e.nom    = 'Nom obligatoire';
  if (!f.prenom.trim()) e.prenom = 'Prénom obligatoire';
  if (f.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(f.email)) e.email = 'Email invalide';
  return e;
}

export default function Participants() {
  const [items,      setItems]      = useState([]);
  const [structures, setStructures] = useState([]);
  const [profils,    setProfils]    = useState([]);
  const [loading,    setLoading]    = useState(true);
  const [apiErr,     setApiErr]     = useState('');
  const [search,     setSearch]     = useState('');
  const [filterProf, setFilterProf] = useState('');

  const [modal,    setModal]    = useState(false);
  const [editItem, setEditItem] = useState(null);
  const [form,     setForm]     = useState(EMPTY);
  const [errors,   setErrors]   = useState({});

  const load = useCallback(async () => {
    try {
      const [p, s, pr] = await Promise.all([
        api.get('/participants'), api.get('/structures'), api.get('/profils'),
      ]);
      setItems(p.data); setStructures(s.data); setProfils(pr.data);
      setApiErr('');
    } catch {
      setApiErr('Impossible de charger les données. Vérifiez que le serveur tourne sur le port 8080.');
    } finally { setLoading(false); }
  }, []);

  useEffect(() => { load(); }, [load]);

  const openCreate = () => { setEditItem(null); setForm(EMPTY); setErrors({}); setModal(true); };
  const openEdit   = (item) => {
    setEditItem(item);
    setForm({ nom:item.nom, prenom:item.prenom, email:item.email||'', tel:item.tel||'',
      idStructure:item.structure?.id||'', idProfil:item.profil?.id||'' });
    setErrors({}); setModal(true);
  };
  const closeModal = () => { setModal(false); setEditItem(null); };

  const handleChange = (e) => {
    setForm(p => ({ ...p, [e.target.name]: e.target.value }));
    setErrors(p => ({ ...p, [e.target.name]: '' }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate(form);
    if (Object.keys(errs).length) { setErrors(errs); return; }
    const payload = {
      nom: form.nom.trim(), prenom: form.prenom.trim(),
      email: form.email.trim() || null, tel: form.tel.trim() || null,
      structure: form.idStructure ? { id: Number(form.idStructure) } : null,
      profil:    form.idProfil    ? { id: Number(form.idProfil) }    : null,
    };
    try {
      if (editItem) await api.put(`/participants/${editItem.id}`, payload);
      else          await api.post('/participants', payload);
      await load(); closeModal();
    } catch (err) {
      setErrors({ _global: err.response?.data || 'Erreur serveur.' });
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer ce participant ?')) return;
    try { await api.delete(`/participants/${id}`); await load(); }
    catch { alert('Suppression impossible.'); }
  };

  const filtered = items.filter(i => {
    const q = search.toLowerCase();
    const matchSearch = `${i.nom} ${i.prenom} ${i.email||''}`.toLowerCase().includes(q);
    const matchProf   = !filterProf || String(i.profil?.id) === filterProf;
    return matchSearch && matchProf;
  });

  const fCount = (n) => n >= 4 ? 'b-rose' : n >= 2 ? 'b-amber' : 'b-green';

  if (loading) return <div className="loading-state">Chargement des participants…</div>;

  return (
    <div>
      <div className="pg-header">
        <div>
          <h1 className="pg-title">Participants</h1>
          <p className="pg-subtitle">{items.length} participant(s) — limite : 4 formations par participant</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>+ Ajouter</button>
      </div>

      {apiErr && <div className="alert alert-err">{apiErr}</div>}

      <div className="toolbar">
        <div className="search-wrap">
          <input className="search-input" placeholder="Rechercher par nom, prénom, email…"
            value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <select className="sel-filter" value={filterProf} onChange={e => setFilterProf(e.target.value)}>
          <option value="">Tous les profils</option>
          {profils.map(p => <option key={p.id} value={p.id}>{p.libelle}</option>)}
        </select>
        <span className="count-chip">{filtered.length} résultat(s)</span>
      </div>

      <div className="tbl-wrap">
        <table className="tbl">
          <thead>
            <tr><th>#</th><th>Participant</th><th>Email</th><th>Tél</th><th>Structure</th><th>Profil</th><th>Formations</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {filtered.map(p => {
              const count = p.formations?.length || 0;
              return (
                <tr key={p.id}>
                  <td style={{color:'var(--txt-3)'}}>{p.id}</td>
                  <td>
                    <div className="av-cell">
                      <div className="av">{p.nom[0]}</div>
                      <div>
                        <div className="av-name">{p.nom} {p.prenom}</div>
                        <div className="av-sub">{p.tel || ''}</div>
                      </div>
                    </div>
                  </td>
                  <td style={{color:'var(--txt-2)',fontSize:12}}>{p.email || '—'}</td>
                  <td style={{color:'var(--txt-2)',fontSize:12}}>{p.tel || '—'}</td>
                  <td><span className="badge b-amber">{p.structure?.libelle || '—'}</span></td>
                  <td><span className="badge b-purple">{p.profil?.libelle || '—'}</span></td>
                  <td><span className={`badge ${fCount(count)}`}>{count} / 4</span></td>
                  <td>
                    <div style={{display:'flex', gap:5}}>
                      <button className="btn btn-warn btn-sm"   onClick={() => openEdit(p)}>✏ Modifier</button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(p.id)}>🗑</button>
                    </div>
                  </td>
                </tr>
              );
            })}
            {filtered.length === 0 && <tr className="tbl-empty"><td colSpan={8}>Aucun participant trouvé</td></tr>}
          </tbody>
        </table>
      </div>

      {modal && (
        <div className="modal-backdrop" onClick={e => e.target === e.currentTarget && closeModal()}>
          <div className="modal">
            <div className="modal-head">
              <h2 className="modal-title">{editItem ? 'Modifier' : 'Ajouter'} un participant</h2>
              <button className="modal-close" onClick={closeModal}>✕</button>
            </div>
            {errors._global && <div className="alert alert-err">{errors._global}</div>}
            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="form-field">
                  <label>Nom *</label>
                  <input name="nom" value={form.nom} onChange={handleChange}
                    placeholder="Ben Amor" className={errors.nom?'err':''} autoFocus />
                  {errors.nom && <span className="err-msg">{errors.nom}</span>}
                </div>
                <div className="form-field">
                  <label>Prénom *</label>
                  <input name="prenom" value={form.prenom} onChange={handleChange}
                    placeholder="Ahmed" className={errors.prenom?'err':''} />
                  {errors.prenom && <span className="err-msg">{errors.prenom}</span>}
                </div>
                <div className="form-field">
                  <label>Email</label>
                  <input name="email" value={form.email} onChange={handleChange}
                    placeholder="email@greenbuilding.tn" className={errors.email?'err':''} />
                  {errors.email && <span className="err-msg">{errors.email}</span>}
                </div>
                <div className="form-field">
                  <label>Téléphone</label>
                  <input name="tel" value={form.tel} onChange={handleChange}
                    placeholder="+216 XX XXX XXX" />
                </div>
                <div className="form-field">
                  <label>Structure</label>
                  <select name="idStructure" value={form.idStructure} onChange={handleChange}>
                    <option value="">— Sélectionner —</option>
                    {structures.map(s => <option key={s.id} value={s.id}>{s.libelle}</option>)}
                  </select>
                </div>
                <div className="form-field">
                  <label>Profil</label>
                  <select name="idProfil" value={form.idProfil} onChange={handleChange}>
                    <option value="">— Sélectionner —</option>
                    {profils.map(p => <option key={p.id} value={p.id}>{p.libelle}</option>)}
                  </select>
                </div>
              </div>
              <div className="form-footer">
                <button type="button" className="btn btn-secondary" onClick={closeModal}>Annuler</button>
                <button type="submit" className="btn btn-primary">{editItem ? 'Enregistrer' : 'Créer'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
