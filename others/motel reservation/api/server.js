const express = require('express');
const fs      = require('fs');
const path    = require('path');

const app       = express();
const PORT      = 8080;
const DATA_FILE = path.join(__dirname, 'data.json');

app.use(express.json());

// CORS
app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
  if (req.method === 'OPTIONS') return res.sendStatus(204);
  next();
});

function loadData() {
  if (!fs.existsSync(DATA_FILE)) return [];
  try { return JSON.parse(fs.readFileSync(DATA_FILE, 'utf8')); } catch { return []; }
}

function saveData(data) {
  fs.writeFileSync(DATA_FILE, JSON.stringify(data, null, 2), 'utf8');
}

function validate(d) {
  const taille      = parseInt(d.taille) || 0;
  const sejour      = parseInt(d.sejour) || 0;
  const nom         = (d.nom || '').trim();
  const commentaire = d.commentaire || '';
  return taille >= 1 && taille <= 4
      && sejour >= 1 && sejour <= 10
      && nom.length >= 2
      && commentaire.length <= 120;
}

function sanitize(d) {
  return {
    id:          parseInt(d.id) || 0,
    nom:         (d.nom || '').trim().replace(/[|<>"']/g, ''),
    taille:      parseInt(d.taille) || 1,
    sejour:      parseInt(d.sejour) || 1,
    commentaire: (d.commentaire || '').trim().replace(/[|<>"']/g, ''),
    chambre:     (d.chambre || '').trim(),
    dateArrivee: (d.dateArrivee || '').trim(),
  };
}

// GET — liste toutes les réservations
app.get('/', (req, res) => {
  res.json(loadData());
});

// POST — nouvelle réservation
app.post('/', (req, res) => {
  if (!validate(req.body)) return res.status(400).json({ error: 'Données invalides.' });
  const data  = loadData();
  const maxId = data.length ? Math.max(...data.map(i => i.id)) : 0;
  req.body.id = maxId + 1;
  const item  = sanitize(req.body);
  data.push(item);
  saveData(data);
  res.status(201).json(item);
});

// PUT — modifier une réservation
app.put('/', (req, res) => {
  const id = parseInt(req.query.id) || 0;
  if (id <= 0 || !validate(req.body)) return res.status(400).json({ error: 'Données invalides.' });
  const data = loadData();
  const idx  = data.findIndex(i => i.id === id);
  if (idx === -1) return res.status(404).json({ error: 'Introuvable.' });
  req.body.id = id;
  data[idx]   = sanitize(req.body);
  saveData(data);
  res.json({ success: true });
});

// DELETE — supprimer une réservation
app.delete('/', (req, res) => {
  const id   = parseInt(req.query.id) || 0;
  const data = loadData().filter(i => i.id !== id);
  saveData(data);
  res.json({ success: true });
});

app.listen(PORT, () => {
  console.log(`API running at http://localhost:${PORT}`);
});
