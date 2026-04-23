export default {
  DB_HOST: process.env.DB_HOST || '127.0.0.1',
  DB_PORT: process.env.DB_PORT || 27017,
  DB_NAME: process.env.DB_NAME || 'gestionEtudiants',
  DB_URI: process.env.DB_URI || `mongodb://127.0.0.1:27017/gestionEtudiants`,
  SERVER_PORT: process.env.PORT || 3000
};