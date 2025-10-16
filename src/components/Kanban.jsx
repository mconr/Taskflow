import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import { setAuthToken } from '../api';


function Column({ title, tasks, onDrop, onDragStart }){
return (
<div className="flex-1 p-4">
<h3 className="font-semibold mb-2">{title}</h3>
<div className="min-h-[200px] bg-white p-2 rounded shadow" onDragOver={e=>e.preventDefault()} onDrop={onDrop}>
{tasks.map(t=> (
<div key={t.id} draggable onDragStart={e=>onDragStart(e, t)} className="p-2 mb-2 border rounded bg-gray-50">
<div className="font-medium">{t.titre}</div>
<div className="text-sm text-gray-600">{t.description}</div>
</div>
))}
</div>
</div>
)
}


export default function Kanban({ onLogout }){
const navigate = useNavigate();
const [tasks, setTasks] = useState([]);
const [loading, setLoading] = useState(true);
const [creating, setCreating] = useState(false);
const [error, setError] = useState(null);
const [newTitle, setNewTitle] = useState('');
const [newDesc, setNewDesc] = useState('');


useEffect(()=>{ fetchTasks(); }, []);


async function fetchTasks(){
try{
	const res = await api.get('/tasks');
	setTasks(res.data);
	setLoading(false);
} catch(e){
	console.error(e);
	// If unauthorized, redirect to login so user can re-authenticate
	if (e.response && (e.response.status === 401 || e.response.status === 403)) {
		// clear any stored token and navigate to login
		setAuthToken(null);
		localStorage.removeItem('tf_token');
		navigate('/login');
		return;
	}
	// stop the loading indicator even on error
	setLoading(false);
}
}


async function createTask(e){
e.preventDefault();
// simple validation
if (!newTitle || newTitle.trim() === ''){
	setError('Le titre est requis');
	return;
}
setError(null);
setCreating(true);
console.log('Creating task', { titre: newTitle, description: newDesc });
try{
	const res = await api.post('/tasks', { titre: newTitle, description: newDesc });
	// clear inputs and refresh the list
	setNewTitle('');
	setNewDesc('');
	await fetchTasks();
	console.log('Task created', res.data);
} catch(err){
	console.error('Create task failed', err);
	const status = err?.response?.status;
	const data = err?.response?.data;
	const body = data ? (typeof data === 'object' ? JSON.stringify(data) : String(data)) : '';
	setError(`Erreur lors de la création${status ? ' ('+status+')' : ''}${body ? ': '+body : ''}`);
} finally{ setCreating(false); }
}


function onDragStart(e, task){ e.dataTransfer.setData('text/plain', JSON.stringify(task)); }


function onDropFactory(status){
	return async function(e){
e.preventDefault();
const data = JSON.parse(e.dataTransfer.getData('text/plain'));
await api.put(`/tasks/${data.id}`, { ...data, statut: status });
fetchTasks();
}
}


const todo = tasks.filter(t=>t.statut==='TODO');
const inprog = tasks.filter(t=>t.statut==='IN_PROGRESS');
const done = tasks.filter(t=>t.statut==='DONE');


if (loading) return <div className="p-6">Loading...</div>


return (
<div className="p-6">
<div className="flex justify-between items-center mb-4">
<h1 className="text-2xl font-bold">TaskFlow Pro — Kanban</h1>
<div>
<button className="px-3 py-1 mr-2 border" onClick={onLogout}>Logout</button>
</div>
</div>


<form onSubmit={createTask} className="mb-4 flex gap-2">
	<input value={newTitle} onChange={e=>setNewTitle(e.target.value)} className="p-2 border flex-1" placeholder="Titre" />
	<input value={newDesc} onChange={e=>setNewDesc(e.target.value)} className="p-2 border flex-2" placeholder="Description" />
	<button type="submit" disabled={creating} className="p-2 bg-blue-600 text-white rounded">{creating ? 'Ajout...' : 'Ajouter'}</button>
</form>
{error && <div className="text-red-600 mb-2">{error}</div>}


<div className="flex gap-4">
<Column title="TODO" tasks={todo} onDrop={onDropFactory('TODO')} onDragStart={onDragStart} />
<Column title="IN_PROGRESS" tasks={inprog} onDrop={onDropFactory('IN_PROGRESS')} onDragStart={onDragStart} />
<Column title="DONE" tasks={done} onDrop={onDropFactory('DONE')} onDragStart={onDragStart} />
</div>
</div>
)
}