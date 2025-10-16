import React, {useState} from 'react';
import api from '../api';
import { useNavigate } from 'react-router-dom';


export default function Register(){
const [username, setUsername] = useState('');
const [password, setPassword] = useState('');
const [msg, setMsg] = useState(null);
const nav = useNavigate();


async function submit(e){
e.preventDefault();
try{
await api.post('/auth/register', { username, password });
setMsg('Enregistré. Connecte-toi.');
setTimeout(()=>nav('/login'), 1000);
}catch(err){ setMsg(err.response?.data?.error || 'Erreur'); }
}


return (
<div className="max-w-md mx-auto mt-20 p-6 bg-white rounded shadow">
<h2 className="text-xl font-semibold mb-4">S'inscrire</h2>
{msg && <div>{msg}</div>}
<form onSubmit={submit}>
<input className="w-full p-2 border mb-2" placeholder="username" value={username} onChange={e=>setUsername(e.target.value)}/>
<input className="w-full p-2 border mb-2" placeholder="password" type="password" value={password} onChange={e=>setPassword(e.target.value)}/>
<button className="w-full p-2 bg-green-600 text-white rounded" type="submit">Register</button>
</form>
</div>
)
}