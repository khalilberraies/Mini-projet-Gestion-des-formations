import React, { useState, useEffect, useCallback } from 'react';
import api from '../api/api';

const EMPTY_FORM = {
  titre: '', annee: new Date().getFullYear(), duree: '',
  budget: '', idDomaine: '', idFormateur: '',
};

function validate(f) {
  const e = {};
  if (!f.titre.trim())        e.titre   = 'Titre obligatoire';
  if (!f.annee)               e.annee   = 'Année obligatoire';
  if (!f.duree || f.duree<1)  e.duree   = 'Durée obligatoire (≥ 1 jour)';
  return e;
}

export default function Formations() {
  const [items,      setItems]      = useState([]);
  const [domaines,   setDomaines]   = useState([]);
  const [formateurs, setFormateurs] = useState([]);
  const [allParts,   setAllParts]   = useState([]);
  const [loading,    setLoading]    = useState(true);
  const [apiErr,     setApiErr]     = useState('');

  const [search,     setSearch]     = useState('');
  const [yearFilter, setYearFilter] = useState('');

  const [modal,      setModal]      = useState(false);
  const [editItem,   setEditItem]   = useState(null);
  const [form,       setForm]       = useState(EMPTY_FORM);
  const [errors,     setErrors]     = useState({});

  const [partModal,  setPartModal]  = useState(null); // formation whose participants we're managing

  const load = useCallback(async () => {
    try {
      const [f, d, fo, p] = await Promise.all([
        api.get('/formations'), api.get('/domaines'),
        api.get('/formateurs'), api.get('/participants'),
      ]);
      setItems(f.data); setDomaines(d.data); setFormateurs(fo.data); setAllParts(p.data);
      setApiErr('');
    } catch {
      setApiErr('Impossible de charger les données. Vérifiez que le serveur tourne sur le port 8080.');
    } finally { setLoading(false); }
  }, []);

  useEffect(() => { load(); }, [load]);

  const openCreate = () => { setEditItem(null); setForm(EMPTY_FORM); setErrors({}); setModal(true); };
  const openEdit   = (item) => {
    setEditItem(item);
    setForm({
      titre: item.titre, annee: item.annee, duree: item.duree,
      budget: item.budget ?? '', idDomaine: item.domaine?.id ?? '',
      idFormateur: item.formateur?.id ?? '',
    });
    setErrors({}); setModal(true);
  };
  const closeModal = () => { setModal(false); setEditItem(null); };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(p => ({ ...p, [name]: value }));
    setErrors(p => ({ ...p, [name]: '' }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errs = validate(form);
    if (Object.keys(errs).length) { setErrors(errs); return; }
    const payload = {
      titre: form.titre.trim(),
      annee: Number(form.annee),
      duree: Number(form.duree),
      budget: form.budget !== '' ? Number(form.budget) : null,
      domaine:   form.idDomaine   ? { id: Number(form.idDomaine) }   : null,
      formateur: form.idFormateur ? { id: Number(form.idFormateur) } : null,
    };
    try {
      if (editItem) await api.put(`/formations/${editItem.id}`, payload);
      else          await api.post('/formations', payload);
      await load(); closeModal();
    } catch (err) {
      setErrors({ _global: err.response?.data || 'Erreur serveur.' });
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Supprimer cette formation ?')) return;
    try { await api.delete(`/formations/${id}`); await load(); }
    catch { alert('Suppression impossible.'); }
  };

  const addParticipant = async (formationId, participantId) => {
    try {
      await api.post(`/formations/${formationId}/participants/${participantId}`);
      await load();
      // refresh the modal data
      const updated = items.find(f => f.id === formationId);
      if (updated) {
        const res = await api.get(`/formations/${formationId}`);
        setPartModal(res.data);
      }
    } catch (err) {
      alert(err.response?.data || 'Inscription impossible. Le participant a peut-être atteint la limite de 4 formations.');
    }
  };

  const years = [...new Set(items.map(i => i.annee))].sort((a,b) => b - a);

  const filtered = items.filter(i => {
    const q = search.toLowerCase();
    const matchSearch = i.titre.toLowerCase().includes(q)
      || (i.domaine?.libelle || '').toLowerCase().includes(q)
      || (i.formateur ? `${i.formateur.nom} ${i.formateur.prenom}` : '').toLowerCase().includes(q);
    const matchYear = !yearFilter || String(i.annee) === yearFilter;
    return matchSearch && matchYear;
  });

  if (loading) return <div className="loading-state">Chargement des formations…</div>;

  return (
    <div>
      <div className="pg-header">
        <div>
          <h1 className="pg-title">Formations</h1>
          <p className="pg-subtitle">{items.length} formation(s) — max 4 participants différents / formation</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>+ Ajouter</button>
      </div>

      {apiErr && <div className="alert alert-err">{apiErr}</div>}

      <div className="toolbar">
        <div className="search-wrap">
          <input className="search-input" placeholder="Rechercher par titre, domaine, formateur…"
            value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <select className="sel-filter" value={yearFilter} onChange={e => setYearFilter(e.target.value)}>
          <option value="">Toutes les années</option>
          {years.map(y => <option key={y} value={y}>{y}</option>)}
        </select>
        <span className="count-chip">{filtered.length} résultat(s)</span>
      </div>

      <div className="tbl-wrap">
        <table className="tbl">
          <thead>
            <tr>
              <th>#</th><th>Titre</th><th>Domaine</th><th>Formateur</th>
              <th>Année</th><th>Durée</th><th>Budget</th><th>Participants</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map(f => (
              <tr key={f.id}>
                <td style={{color:'var(--txt-3)'}}>{f.id}</td>
                <td style={{fontWeight:600, maxWidth:210, overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap'}}>{f.titre}</td>
                <td><span className="badge b-blue">{f.domaine?.libelle || '—'}</span></td>
                <td style={{color:'var(--txt-2)'}}>
                  {f.formateur
                    ? <span><strong style={{color:'var(--txt-1)'}}>{f.formateur.nom}</strong> {f.formateur.prenom}<br/>
                        <span className={`badge ${f.formateur.type==='INTERNE'?'b-teal':'b-purple'}`} style={{fontSize:10}}>
                          {f.formateur.type}
                        </span></span>
                    : '—'
                  }
                </td>
                <td>{f.annee}</td>
                <td>{f.duree} j</td>
                <td style={{color:'var(--green)'}}>
                  {f.budget != null ? f.budget.toLocaleString('fr-TN') + ' TND' : '—'}
                </td>
                <td>
                  <button className="btn btn-secondary btn-sm" onClick={() => setPartModal(f)}>
                    👥 {f.participants?.length || 0}
                  </button>
                </td>
                <td>
                  <div style={{display:'flex', gap:5}}>
                    <button className="btn btn-warn btn-sm"   onClick={() => openEdit(f)}>✏</button>
                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(f.id)}>🗑</button>
                  </div>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && <tr className="tbl-empty"><td colSpan={9}>Aucune formation trouvée</td></tr>}
          </tbody>
        </table>
      </div>

      {/* ── Formation form modal ── */}
      {modal && (
        <div className="modal-backdrop" onClick={e => e.target === e.currentTarget && closeModal()}>
          <div className="modal wide">
            <div className="modal-head">
              <h2 className="modal-title">{editItem ? 'Modifier' : 'Ajouter'} une formation</h2>
              <button className="modal-close" onClick={closeModal}>✕</button>
            </div>
            {errors._global && <div className="alert alert-err">{errors._global}</div>}
            <form onSubmit={handleSubmit}>
              <div className="form-grid">
                <div className="form-field col-2">
                  <label>Titre *</label>
                  <input name="titre" value={form.titre}
                    onChange={handleChange} placeholder="ex : Développement Java EE Avancé"
                    className={errors.titre ? 'err' : ''} autoFocus />
                  {errors.titre && <span className="err-msg">{errors.titre}</span>}
                </div>
                <div className="form-field">
                  <label>Année *</label>
                  <input type="number" name="annee" value={form.annee}
                    onChange={handleChange} min="2000" max="2100"
                    className={errors.annee ? 'err' : ''} />
                  {errors.annee && <span className="err-msg">{errors.annee}</span>}
                </div>
                <div className="form-field">
                  <label>Durée (jours) *</label>
                  <input type="number" name="duree" value={form.duree}
                    onChange={handleChange} min="1" placeholder="ex : 5"
                    className={errors.duree ? 'err' : ''} />
                  {errors.duree && <span className="err-msg">{errors.duree}</span>}
                </div>
                <div className="form-field">
                  <label>Budget (TND)</label>
                  <input type="number" name="budget" value={form.budget}
                    onChange={handleChange} min="0" step="100" placeholder="ex : 4500" />
                </div>
                <div className="form-field">
                  <label>Domaine</label>
                  <select name="idDomaine" value={form.idDomaine} onChange={handleChange}>
                    <option value="">— Sélectionner —</option>
                    {domaines.map(d => <option key={d.id} value={d.id}>{d.libelle}</option>)}
                  </select>
                </div>
                <div className="form-field col-2">
                  <label>Formateur</label>
                  <select name="idFormateur" value={form.idFormateur} onChange={handleChange}>
                    <option value="">— Sélectionner —</option>
                    {formateurs.map(f => (
                      <option key={f.id} value={f.id}>
                        {f.nom} {f.prenom} — {f.type}{f.employeur ? ` (${f.employeur.nomEmployeur})` : ''}
                      </option>
                    ))}
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

      {/* ── Participants modal ── */}
      {partModal && (
        <div className="modal-backdrop" onClick={e => e.target === e.currentTarget && setPartModal(null)}>
          <div className="modal wide">
            <div className="modal-head">
              <div>
                <h2 className="modal-title">Participants — {partModal.titre}</h2>
                <p style={{fontSize:12,color:'var(--txt-3)',marginTop:2}}>{partModal.annee} · {partModal.duree} jour(s)</p>
              </div>
              <button className="modal-close" onClick={() => setPartModal(null)}>✕</button>
            </div>

            {/* Inscrits */}
            <div style={{marginBottom:20}}>
              <p style={{fontSize:11,color:'var(--txt-3)',textTransform:'uppercase',letterSpacing:'.07em',fontWeight:600,marginBottom:10}}>
                Inscrits ({(partModal.participants || []).length})
              </p>
              {(partModal.participants || []).length === 0
                ? <p style={{color:'var(--txt-3)',fontStyle:'italic',fontSize:13}}>Aucun participant inscrit</p>
                : (partModal.participants || []).map(p => (
                  <div key={p.id} style={{display:'flex',alignItems:'center',gap:10,padding:'8px 0',borderBottom:'1px solid var(--border)'}}>
                    <div className="av" style={{flexShrink:0}}>{p.nom[0]}</div>
                    <div>
                      <span style={{fontWeight:600}}>{p.nom} {p.prenom}</span>
                      <span style={{color:'var(--txt-3)',fontSize:12,marginLeft:8}}>{p.profil?.libelle}</span>
                    </div>
                  </div>
                ))
              }
            </div>

            {/* Ajouter */}
            <div>
              <p style={{fontSize:11,color:'var(--txt-3)',textTransform:'uppercase',letterSpacing:'.07em',fontWeight:600,marginBottom:10}}>
                Ajouter un participant
              </p>
              <div style={{maxHeight:200,overflowY:'auto',display:'flex',flexDirection:'column',gap:6}}>
                {allParts
                  .filter(p => !(partModal.participants || []).some(sp => sp.id === p.id))
                  .map(p => {
                    const count = p.formations?.length || 0;
                    const full  = count >= 4;
                    return (
                      <div key={p.id} style={{
                        display:'flex',alignItems:'center',justifyContent:'space-between',
                        padding:'8px 12px',background:'var(--bg-raised)',
                        borderRadius:'var(--radius)',border:'1px solid var(--border)',
                        opacity: full ? .5 : 1,
                      }}>
                        <div style={{display:'flex',alignItems:'center',gap:10}}>
                          <div className="av" style={{width:28,height:28,fontSize:11}}>{p.nom[0]}</div>
                          <div>
                            <span style={{fontWeight:500,fontSize:13}}>{p.nom} {p.prenom}</span>
                            <span style={{color:'var(--txt-3)',fontSize:11,marginLeft:8}}>{p.structure?.libelle}</span>
                          </div>
                        </div>
                        <div style={{display:'flex',alignItems:'center',gap:8}}>
                          <span className={`badge ${count>=4?'b-rose':count>=2?'b-amber':'b-green'}`}>
                            {count}/4
                          </span>
                          <button
                            className="btn btn-primary btn-sm"
                            disabled={full}
                            onClick={() => addParticipant(partModal.id, p.id)}
                          >
                            {full ? 'Complet' : '+ Inscrire'}
                          </button>
                        </div>
                      </div>
                    );
                  })
                }
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
