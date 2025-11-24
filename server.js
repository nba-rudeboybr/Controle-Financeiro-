const express = require('express');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Servir arquivos estáticos (CSS, JS, imagens, etc.)
app.use(express.static(__dirname, {
  index: false, // Não servir index.html automaticamente
  extensions: ['html', 'css', 'js', 'json', 'png', 'jpg', 'jpeg', 'gif', 'svg']
}));

// Rota raiz - servir index.html
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

// Rota catch-all para SPA - servir index.html em todas as outras rotas
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor frontend rodando na porta ${PORT}`);
  console.log(`Acesse: http://localhost:${PORT}`);
});

