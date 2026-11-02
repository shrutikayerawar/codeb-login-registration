import React, {useEffect, useState} from 'react';
const API = import.meta.env.VITE_API_URL || 'http://localhost:8080';
async function request(path, body, token) {
  const res = await fetch(`${API}${path}`, {method: body ? 'POST':'GET', headers:{'Content-Type':'application/json', ...(token?{Authorization:`Bearer ${token}`}:{})}, ...(body?{body:JSON.stringify(body)}:{})});
  const data = await res.json().catch(()=>({message:'Unexpected server response'}));
  if(!res.ok) throw new Error(data.message || data.error || 'Request failed');
  return data;
}
export default function App(){
  const [page,setPage]=useState(new URLSearchParams(location.search).has('token') && location.pathname.includes('reset') ? 'reset' : 'login'), [form,setForm]=useState({fullName:'',email:'',password:'',role:'SALES_PERSON'}), [message,setMessage]=useState(''), [error,setError]=useState(''), [user,setUser]=useState(null), [token,setToken]=useState(localStorage.getItem('codeb_token')||'');
  const [resetToken,setResetToken]=useState(new URLSearchParams(location.search).get('token')||'');
  const [newPassword,setNewPassword]=useState('');
  function update(k,v){setForm(f=>({...f,[k]:v}));}
  function logout(){localStorage.removeItem('codeb_token');setToken('');setUser(null);setPage('login');setMessage('You have been logged out.');}
  useEffect(()=>{const params=new URLSearchParams(location.search); const verificationToken=params.get('token'); if(location.pathname.includes('verify-email') && verificationToken){request('/api/auth/verify-email',{token:verificationToken}).then(d=>{setMessage(d.message);setPage('login');history.replaceState({},'', '/');}).catch(e=>{setError(e.message);setPage('login');});}},[]);
  useEffect(()=>{if(!token)return; request('/api/auth/me',null,token).then(setUser).catch(logout);},[]);
  useEffect(()=>{if(!token)return; let timer; const reset=()=>{clearTimeout(timer);timer=setTimeout(logout,15*60*1000);}; ['mousemove','keydown','click','scroll'].forEach(e=>window.addEventListener(e,reset)); reset(); return()=>{clearTimeout(timer);['mousemove','keydown','click','scroll'].forEach(e=>window.removeEventListener(e,reset));};},[token]);
  async function submit(e){e.preventDefault();setError('');setMessage('');try{
    if(page==='register'){const d=await request('/api/auth/register',{...form});setMessage(d.message);setPage('login');}
    if(page==='login'){const d=await request('/api/auth/login',{email:form.email,password:form.password});localStorage.setItem('codeb_token',d.token);setToken(d.token);setUser(d);setMessage('Login successful.');}
    if(page==='forgot'){const d=await request('/api/auth/forgot-password',{email:form.email});setMessage(d.message);}
    if(page==='reset'){const d=await request('/api/auth/reset-password',{token:resetToken,newPassword});setMessage(d.message);setPage('login');}
  }catch(err){setError(err.message);}}
  if(user) return <main className="shell"><header><div className="brand">CODE-B <span>IMS</span></div><button className="secondary" onClick={logout}>Logout</button></header><section className="welcome"><p className="eyebrow">INTERNAL MANAGEMENT SYSTEM</p><h1>Welcome, {user.fullName || user.full_name || 'User'}</h1><p>You are signed in as <b>{user.role}</b>.</p><div className="panel"><h2>{user.role==='ADMIN'?'Administrator dashboard':'Sales Person dashboard'}</h2><p>{user.role==='ADMIN'?'Admin access is reserved for authorized administrators.':'You have access to the sales-person area.'}</p><p className="muted">This first module establishes secure account access. Additional IMS modules can be added in later topics.</p></div></section></main>;
  return <main className="shell"><header><div className="brand">CODE-B <span>IMS</span></div><span className="secure">SECURE ACCOUNT ACCESS</span></header><section className="auth-layout"><div className="intro"><p className="eyebrow">INTERNAL MANAGEMENT SYSTEM</p><h1>One secure place to manage your work.</h1><p>Sign in to continue to the Code-B management system.</p><div className="feature"><b>Role-based access</b><span>Separate access for administrators and sales persons.</span></div><div className="feature"><b>Protected accounts</b><span>Passwords are stored as secure hashes.</span></div></div><div className="card"><div className="tabs"><button className={page==='login'?'active':''} onClick={()=>{setPage('login');setError('');}}>Login</button><button className={page==='register'?'active':''} onClick={()=>{setPage('register');setError('');}}>Register</button></div>
    <h2>{page==='login'?'Welcome back':page==='register'?'Create your account':page==='forgot'?'Forgot password':'Set a new password'}</h2><p className="muted">{page==='login'?'Enter your credentials to access your dashboard.':'Complete the details below.'}</p>
    {error&&<div className="alert error">{error}</div>}{message&&<div className="alert success">{message}</div>}
    <form onSubmit={submit}>
      {page==='register'&&<label>Full name<input value={form.fullName} onChange={e=>update('fullName',e.target.value)} required maxLength="100" placeholder="Your full name"/></label>}
      {(page==='login'||page==='register'||page==='forgot')&&<label>Email address<input type="email" value={form.email} onChange={e=>update('email',e.target.value)} required placeholder="name@example.com"/></label>}
      {(page==='login'||page==='register')&&<label>Password<input type="password" value={form.password} onChange={e=>update('password',e.target.value)} required minLength="8" placeholder="At least 8 characters"/></label>}
      {page==='register'&&<label>Account role<select value={form.role} onChange={e=>update('role',e.target.value)}><option value="SALES_PERSON">Sales Person</option><option value="ADMIN">Admin (converted to Sales Person for public signup)</option></select><small>Public registration cannot grant administrator privileges.</small></label>}
      {page==='reset'&&<><label>Reset token<input value={resetToken} onChange={e=>setResetToken(e.target.value)} required/></label><label>New password<input type="password" value={newPassword} onChange={e=>setNewPassword(e.target.value)} minLength="8" required/></label></>}
      <button className="primary" type="submit">{page==='login'?'Sign in':page==='register'?'Create account':page==='forgot'?'Send reset instructions':'Update password'}</button>
    </form>
    {page==='login'&&<button className="link" onClick={()=>{setPage('forgot');setMessage('');setError('');}}>Forgot your password?</button>}
    {page==='forgot'&&<button className="link" onClick={()=>setPage('login')}>Back to login</button>}
    {page==='reset'&&<button className="link" onClick={()=>setPage('login')}>Back to login</button>}
    <p className="fine">By continuing, you agree to use this system only with an authorized account.</p>
  </div></section><footer>© Code-B IMS · Login & Registration Module</footer></main>;
}
