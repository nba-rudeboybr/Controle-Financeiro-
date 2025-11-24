# 🚀 Guia de Deploy do Frontend no Render

## ✅ O que foi configurado

1. **Atualização do `script.js`**: Agora detecta automaticamente se está rodando localmente ou em produção e usa a URL correta do backend
2. **Servidor Node.js**: Criado `server.js` e `package.json` para servir o frontend estático
3. **Configuração do Render**: Atualizado `render.yaml` com o serviço do frontend

## 📋 Passo a Passo para Deploy

### Opção 1: Deploy via Render Dashboard (Recomendado)

1. **Acesse o Render Dashboard**
   - Vá para [https://dashboard.render.com](https://dashboard.render.com)
   - Faça login na sua conta

2. **Criar Novo Serviço Web**
   - Clique em "New +" no canto superior direito
   - Selecione "Web Service"

3. **Conectar Repositório**
   - Se seu código está no GitHub/GitLab/Bitbucket, conecte o repositório
   - Ou faça upload manual dos arquivos

4. **Configurar o Serviço**
   - **Name**: `controle-financeiro-frontend`
   - **Environment**: `Node`
   - **Region**: `Oregon` (ou a região mais próxima)
   - **Branch**: `main` (ou a branch que você usa)
   - **Root Directory**: Deixe em branco (raiz do projeto)
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Plan**: `Free`

5. **Deploy**
   - Clique em "Create Web Service"
   - O Render vai instalar as dependências e iniciar o servidor
   - Aguarde alguns minutos até o deploy completar

6. **Obter a URL**
   - Após o deploy, você receberá uma URL como: `https://controle-financeiro-frontend.onrender.com`
   - Esta será a URL do seu frontend!

### Opção 2: Deploy via render.yaml (Blueprint)

Se você já tem o backend configurado via `render.yaml`:

1. **No Render Dashboard**
   - Vá em "Blueprints"
   - Clique em "New Blueprint"
   - Conecte seu repositório
   - O Render detectará automaticamente o `render.yaml` e criará todos os serviços

2. **Aguardar Deploy**
   - O Render criará automaticamente:
     - Backend (API)
     - Banco de dados PostgreSQL
     - Frontend

## 🔧 Verificações Importantes

### 1. Verificar CORS no Backend
O backend já está configurado para aceitar requisições de qualquer origem (`CorsConfig.java`), então não precisa de alterações.

### 2. Testar a Conexão
Após o deploy:
- Acesse a URL do frontend
- Verifique se o status da API aparece como "Online"
- Tente adicionar uma transação para testar

### 3. URLs Configuradas
- **Backend**: `https://controle-financeiro-dl2j.onrender.com`
- **Frontend**: Será algo como `https://controle-financeiro-frontend.onrender.com` (você receberá após o deploy)

## 🐛 Troubleshooting

### Frontend não conecta ao backend
- Verifique se a URL do backend está correta no `script.js`
- Verifique se o backend está rodando e acessível
- Verifique os logs no Render Dashboard

### Erro 404 ao acessar rotas
- O `server.js` já está configurado para redirecionar todas as rotas para `index.html`
- Se ainda assim houver problemas, verifique os logs do servidor

### Erro ao instalar dependências
- Verifique se o `package.json` está na raiz do projeto
- Verifique se o Node.js está configurado corretamente (versão 18+)

## 📝 Notas

- O plano **Free** do Render pode colocar o serviço em "sleep" após 15 minutos de inatividade
- Na primeira requisição após o sleep, pode demorar alguns segundos para "acordar"
- Para evitar isso, considere fazer upgrade para um plano pago ou usar um serviço de "ping" para manter o serviço ativo

## ✨ Pronto!

Após seguir esses passos, seu frontend estará no ar e conectado ao backend! 🎉

