import React, {useState} from 'react';
import api from '../api';


export default function Login({ onLogin }){
const [username, setUsername] = useState('');
const [password, setPassword] = useState('');
const [err, setErr] = useState(null);


async function submit(e){
e.preventDefault();
try{
const res = await api.post('/auth/login', { username, password });
onLogin(res.data.token);
}catch(err){ setErr(err.response?.data?.error || 'Erreur'); }
}


return (
<div className="max-w-md mx-auto mt-20 p-6 bg-white rounded shadow">
<h2 className="text-xl font-semibold mb-4">Se connecter</h2>
{err && <div className="text-red-600">{err}</div>}
<form onSubmit={submit}>
<input className="w-full p-2 border mb-2" placeholder="username" value={username} onChange={e=>setUsername(e.target.value)}/>
<input className="w-full p-2 border mb-2" placeholder="password" type="password" value={password} onChange={e=>setPassword(e.target.value)}/>
<button className="w-full p-2 bg-blue-600 text-white rounded" type="submit">Login</button>
</form>
</div>
)
}