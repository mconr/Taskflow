import React, { useEffect, useState } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import Kanban from './components/Kanban';
import { setAuthToken } from './api';
import axios from 'axios';
import { API_BASE } from './config';


export default function App(){
const [token, setToken] = useState(localStorage.getItem('tf_token'));
const navigate = useNavigate();


useEffect(()=>{ setAuthToken(token); }, [token]);

// DEV: attempt an automatic login with a test user when no token is present
useEffect(()=>{
	if (token) return;
	const loginDev = async () => {
		try{
			const res = await axios.post(`${API_BASE}/auth/login`, { username: 'diag_user_live', password: 'DiagP@ss123' });
			const t = res.data.token;
			setToken(t);
			localStorage.setItem('tf_token', t);
		} catch(e){
			// ignore - user can login manually
			console.debug('Auto-login failed (dev):', e?.response?.status || e.message);
		}
	};
	loginDev();
}, []);


function onLogin(token){
setToken(token); localStorage.setItem('tf_token', token); navigate('/');
}
function onLogout(){ setToken(null); localStorage.removeItem('tf_token'); navigate('/login'); }


return (
<div className="min-h-screen bg-gray-50">
<Routes>
<Route path="/login" element={<Login onLogin={onLogin}/>}/>
<Route path="/register" element={<Register/>}/>
<Route path="/" element={<Kanban onLogout={onLogout}/>}/>
</Routes>
</div>
)
}