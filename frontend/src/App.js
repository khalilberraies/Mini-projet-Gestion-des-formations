import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, NavLink, Navigate } from 'react-router-dom';
import { Building2 } from 'lucide-react';   // icône bâtiment
import './App.css';

// Pages
import Dashboard    from './pages/Dashboard';
import Formations   from './pages/Formations';
import Participants from './pages/Participants';
import Formateurs   from './pages/Formateurs';
import Statistiques from './pages/Statistiques';
import Domaines     from './pages/Domaines';
import Profils      from './pages/Profils';
import Structures   from './pages/Structures';
import Employeurs   from './pages/Employeurs';
import Utilisateurs from './pages/Utilisateurs';

/* ── Role-based navigation config ──────────────────────── */
const SECTIONS = [
  {
    label: 'Général',
    items: [
      { path: '/',             label: 'Tableau de bord', icon: '⬡', roles: ['ADMIN','RESPONSABLE','UTILISATEUR'] },
      { path: '/statistiques', label: 'Statistiques',    icon: '◑', roles: ['ADMIN','RESPONSABLE'] },
    ],
  },
  {
    label: 'Gestion',
    items: [
      { path: '/formations',   label: 'Formations',   icon: '◈', roles: ['ADMIN','UTILISATEUR'] },
      { path: '/participants', label: 'Participants',  icon: '◉', roles: ['ADMIN','UTILISATEUR'] },
      { path: '/formateurs',   label: 'Formateurs',   icon: '◆', roles: ['ADMIN','UTILISATEUR'] },
    ],
  },
  {
    label: 'Administration',
    items: [
      { path: '/utilisateurs', label: 'Utilisateurs', icon: '◎', roles: ['ADMIN'] },
      { path: '/domaines',     label: 'Domaines',     icon: '◐', roles: ['ADMIN'] },
      { path: '/profils',      label: 'Profils',      icon: '◳', roles: ['ADMIN'] },
      { path: '/structures',   label: 'Structures',   icon: '◴', roles: ['ADMIN'] },
      { path: '/employeurs',   label: 'Employeurs',   icon: '◷', roles: ['ADMIN'] },
    ],
  },
];

/* ── Demo users (mirrors the DB seed) ─────────────────── */
const DEMO_USERS = {
  admin:        { role: 'ADMIN',        display: 'Administrateur',    badge: '⊛ ADMIN' },
  responsable:  { role: 'RESPONSABLE',  display: 'Responsable Centre',badge: '⊙ RESP.' },
  utilisateur:  { role: 'UTILISATEUR',  display: 'Utilisateur',       badge: '⊚ USER' },
};

// Mots de passe renforcés (conformes aux exigences)
const DEMO_PWD = { admin:'Admin@2026!', responsable:'Resp@2026!', utilisateur:'User@2026!' };

/* ════════════════════════════════════════════════════════
   LOGIN PAGE
════════════════════════════════════════════════════════ */
function LoginPage({ onLogin }) {
  const [login, setLogin]       = useState('');
  const [password, setPassword] = useState('');
  const [error, setError]       = useState('');

  const submit = (e) => {
    e.preventDefault();
    const user = DEMO_USERS[login.trim().toLowerCase()];
    if (user && password === DEMO_PWD[login.trim().toLowerCase()]) {
      onLogin({ login: login.trim().toLowerCase(), ...user });
    } else {
      setError('Identifiants incorrects. Consultez les comptes démo ci-dessous.');
    }
  };

  return (
    <div className="login-wrap">
      <div className="login-box">
        {/* Logo avec icône Building2 et dégradé teal */}
        <div className="login-logo">
          <Building2 size={32} strokeWidth={1.8} />
        </div>
        <h1 className="login-title">Green Building</h1>
        <p className="login-sub">Centre de formation — An Excellent Training</p>

        <form onSubmit={submit}>
          {error && <div className="login-err">{error}</div>}
          <label className="f-label">Identifiant</label>
          <input
            className={`f-input ${error ? 'is-error' : ''}`}
            value={login}
            onChange={e => { setLogin(e.target.value); setError(''); }}
            placeholder="admin / responsable / utilisateur"
            autoFocus
          />
          <label className="f-label">Mot de passe</label>
          <input
            type="password"
            className={`f-input ${error ? 'is-error' : ''}`}
            value={password}
            onChange={e => { setPassword(e.target.value); setError(''); }}
            placeholder="••••••••"
          />
          <button type="submit" className="btn-login-submit">Se connecter →</button>
        </form>

        <div className="demo-accounts">
          <p>Comptes démo</p>
          <div className="demo-row">
            <span className="demo-role">Administrateur</span>
            <span className="demo-creds">admin / Admin@2026!</span>
          </div>
          <div className="demo-row">
            <span className="demo-role">Responsable</span>
            <span className="demo-creds">responsable / Resp@2026!</span>
          </div>
          <div className="demo-row">
            <span className="demo-role">Utilisateur</span>
            <span className="demo-creds">utilisateur / User@2026!</span>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ════════════════════════════════════════════════════════
   MAIN APP
════════════════════════════════════════════════════════ */
export default function App() {
  const [user, setUser]         = useState(null);
  const [collapsed, setCollapsed] = useState(false);

  if (!user) return <LoginPage onLogin={setUser} />;

  const visibleSections = SECTIONS.map(sec => ({
    ...sec,
    items: sec.items.filter(i => i.roles.includes(user.role)),
  })).filter(sec => sec.items.length > 0);

  return (
    <BrowserRouter>
      <div className={`shell ${collapsed ? 'collapsed' : ''}`}>

        {/* ── Sidebar ── */}
        <aside className="sidebar">
          <div className="sb-header">
            <div className="sb-logo">
              <Building2 size={22} strokeWidth={1.8} />
            </div>
            {!collapsed && (
              <div className="sb-brand">
                <span className="sb-brand-name">Green Building</span>
              
              </div>
            )}
          </div>

          <nav className="sb-nav">
            {visibleSections.map(sec => (
              <React.Fragment key={sec.label}>
                <span className="sb-section-label">{sec.label}</span>
                {sec.items.map(item => (
                  <NavLink
                    key={item.path}
                    to={item.path}
                    end={item.path === '/'}
                    className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}
                    title={collapsed ? item.label : undefined}
                  >
                    <span className="nl-icon">{item.icon}</span>
                    {!collapsed && <span className="nl-text">{item.label}</span>}
                  </NavLink>
                ))}
              </React.Fragment>
            ))}
          </nav>

          <div className="sb-footer">
            <div className="sb-user">
              <div className="sb-avatar">{user.login[0].toUpperCase()}</div>
              {!collapsed && (
                <div>
                  <span className="sb-uname">{user.display}</span>
                  <span className="sb-urole">{user.role}</span>
                </div>
              )}
            </div>
            {!collapsed && (
              <button className="btn-logout" onClick={() => setUser(null)}>
                ↩ Déconnexion
              </button>
            )}
          </div>
        </aside>

        {/* ── Main ── */}
        <div className="main-wrap">
          <header className="topbar">
            <button className="tb-toggle" onClick={() => setCollapsed(p => !p)}>
              {collapsed ? '▶' : '◀'}
            </button>
            <span className="tb-title">Gestion de Formation</span>
            <div className="tb-user">
              <div className="tb-avatar">{user.login[0].toUpperCase()}</div>
              <span>{user.display}</span>
            </div>
          </header>

          <main className="page-body">
            <Routes>
              <Route path="/"             element={<Dashboard user={user} />} />
              <Route path="/formations"   element={<Formations />} />
              <Route path="/participants" element={<Participants />} />
              <Route path="/formateurs"   element={<Formateurs />} />
              <Route path="/statistiques" element={<Statistiques />} />
              <Route path="/domaines"     element={<Domaines />} />
              <Route path="/profils"      element={<Profils />} />
              <Route path="/structures"   element={<Structures />} />
              <Route path="/employeurs"   element={<Employeurs />} />
              <Route path="/utilisateurs" element={<Utilisateurs />} />
              <Route path="*"             element={<Navigate to="/" />} />
            </Routes>
          </main>
        </div>
      </div>
    </BrowserRouter>
  );
}