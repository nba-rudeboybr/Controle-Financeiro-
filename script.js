// Configuração da API
// Usa a URL do backend no Render em produção, ou localhost em desenvolvimento
const BACKEND_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
    ? 'http://localhost:8081'
    : 'https://controle-financeiro-dl2j.onrender.com';

const API_URL = `${BACKEND_URL}/api/transacoes`;
const CATEGORIAS_URL = `${BACKEND_URL}/api/categorias`;

let categorias = [];
let transacoes = [];

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    configurarLinks();
    setDataAtual();
    verificarStatusAPI();
    carregarCategorias();
    carregarTransacoes();
    configurarEventos();
});

// Configurar links dinamicamente baseado no ambiente
function configurarLinks() {
    const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
    
    // Link do Swagger
    const swaggerLink = document.getElementById('swaggerLink');
    if (swaggerLink) {
        swaggerLink.href = isLocalhost 
            ? 'http://localhost:8081/swagger-ui/index.html'
            : 'https://controle-financeiro-dl2j.onrender.com/swagger-ui/index.html';
    }
    
    // Link do H2 Console (só aparece em localhost)
    const h2Link = document.getElementById('h2Link');
    const h2Separator = document.getElementById('h2Separator');
    if (h2Link && h2Separator) {
        if (isLocalhost) {
            h2Link.style.display = 'inline';
            h2Separator.style.display = 'inline';
        } else {
            h2Link.style.display = 'none';
            h2Separator.style.display = 'none';
        }
    }
}

// Configurar data atual
function setDataAtual() {
    const dataInput = document.getElementById('data');
    const hoje = new Date().toISOString().split('T')[0];
    dataInput.value = hoje;
    dataInput.max = hoje; // Não permite data futura
}

// Configurar eventos
function configurarEventos() {
    // Botões do formulário
    const btnReceita = document.querySelector('[data-tipo="RECEITA"]');
    const btnDespesa = document.querySelector('[data-tipo="DESPESA"]');
    
    btnReceita.addEventListener('click', (e) => {
        e.preventDefault();
        adicionarTransacao('RECEITA');
    });
    
    btnDespesa.addEventListener('click', (e) => {
        e.preventDefault();
        adicionarTransacao('DESPESA');
    });

    // Modal
    document.getElementById('modalFechar').addEventListener('click', () => {
        document.getElementById('modalConfirmacao').style.display = 'none';
    });
}

// Verificar status da API
async function verificarStatusAPI() {
    const statusElement = document.getElementById('apiStatus');
    try {
        const response = await fetch(CATEGORIAS_URL);
        if (response.ok) {
            statusElement.textContent = '● API Online';
            statusElement.className = 'status online';
        } else {
            throw new Error('API offline');
        }
    } catch (error) {
        statusElement.textContent = '● API Offline';
        statusElement.className = 'status offline';
    }
}

// Carregar categorias para sugestões
async function carregarCategorias() {
    try {
        const response = await fetch(CATEGORIAS_URL);
        
        if (!response.ok) {
            throw new Error('Erro ao carregar categorias');
        }
        
        categorias = await response.json();
        
        // Se não há categorias, criar algumas padrão primeiro
        if (categorias.length === 0) {
            console.log('Nenhuma categoria encontrada. Criando categorias padrão...');
            await criarCategoriasIniciais();
            // Recarregar após criar
            const response2 = await fetch(CATEGORIAS_URL);
            categorias = await response2.json();
        }
        
        // Preencher datalist com sugestões
        const datalist = document.getElementById('categoriasList');
        datalist.innerHTML = '';
        
        categorias.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.nome;
            option.textContent = `${cat.tipo === 'RECEITA' ? '💰' : '💸'} ${cat.nome}`;
            datalist.appendChild(option);
        });
        
        console.log(`${categorias.length} categorias carregadas como sugestões`);
        
    } catch (error) {
        console.error('Erro ao carregar categorias:', error);
    }
}

// Criar categorias iniciais
async function criarCategoriasIniciais() {
    const categoriasIniciais = [
        { nome: 'Salário', descricao: 'Salário mensal', tipo: 'RECEITA', cor: '#4CAF50' },
        { nome: 'Freelance', descricao: 'Trabalhos extras', tipo: 'RECEITA', cor: '#8BC34A' },
        { nome: 'Investimentos', descricao: 'Rendimentos', tipo: 'RECEITA', cor: '#009688' },
        { nome: 'Alimentação', descricao: 'Gastos com alimentação', tipo: 'DESPESA', cor: '#FF5722' },
        { nome: 'Transporte', descricao: 'Gastos com transporte', tipo: 'DESPESA', cor: '#FF9800' },
        { nome: 'Moradia', descricao: 'Aluguel, contas, etc', tipo: 'DESPESA', cor: '#F44336' },
        { nome: 'Lazer', descricao: 'Entretenimento', tipo: 'DESPESA', cor: '#9C27B0' }
    ];

    console.log('Criando categorias padrão...');
    
    for (const cat of categoriasIniciais) {
        try {
            const response = await fetch(CATEGORIAS_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(cat)
            });
            
            if (response.ok) {
                console.log(`✓ Categoria "${cat.nome}" criada`);
            } else {
                console.error(`✗ Erro ao criar categoria "${cat.nome}"`);
            }
        } catch (error) {
            console.error('Erro ao criar categoria:', error);
        }
    }
    
    console.log('Categorias padrão criadas com sucesso!');
}

// Carregar transações
async function carregarTransacoes() {
    try {
        const response = await fetch(API_URL);
        
        if (!response.ok) {
            throw new Error('Erro ao carregar transações');
        }
        
        transacoes = await response.json();
        
        renderizarTransacoes();
        atualizarResumo();
        
    } catch (error) {
        console.error('Erro ao carregar transações:', error);
        document.getElementById('transacoesBody').innerHTML = `
            <tr><td colspan="6" class="error">Erro ao carregar transações</td></tr>
        `;
    }
}

// Renderizar transações na tabela
function renderizarTransacoes() {
    const tbody = document.getElementById('transacoesBody');
    
    if (transacoes.length === 0) {
        tbody.innerHTML = `
            <tr><td colspan="6" class="empty">Nenhuma transação cadastrada</td></tr>
        `;
        return;
    }

    // Ordenar por data (mais recente primeiro)
    const ordenadas = [...transacoes].sort((a, b) => 
        new Date(b.data) - new Date(a.data)
    );

    tbody.innerHTML = ordenadas.map(t => `
        <tr>
            <td>${formatarData(t.data)}</td>
            <td><strong>${t.descricao}</strong></td>
            <td>
                <span class="badge" style="background: ${t.categoriaCor || '#999'}">
                    ${t.categoriaNome || 'Sem categoria'}
                </span>
            </td>
            <td>
                <span class="badge ${t.tipo.toLowerCase()}">
                    ${t.tipo === 'RECEITA' ? '💰' : '💸'} ${t.tipo}
                </span>
            </td>
            <td class="valor ${t.tipo.toLowerCase()}">
                ${t.tipo === 'RECEITA' ? '+' : '-'} ${formatarMoeda(t.valor)}
            </td>
            <td>
                <button class="btn-icon" onclick="deletarTransacao(${t.id})" title="Deletar">
                    🗑️
                </button>
            </td>
        </tr>
    `).join('');
}

// Atualizar resumo financeiro
function atualizarResumo() {
    const totalReceitas = transacoes
        .filter(t => t.tipo === 'RECEITA')
        .reduce((sum, t) => sum + t.valor, 0);
    
    const totalDespesas = transacoes
        .filter(t => t.tipo === 'DESPESA')
        .reduce((sum, t) => sum + t.valor, 0);
    
    const saldo = totalReceitas - totalDespesas;

    document.getElementById('totalReceitas').textContent = formatarMoeda(totalReceitas);
    document.getElementById('totalDespesas').textContent = formatarMoeda(totalDespesas);
    
    const saldoElement = document.getElementById('saldo');
    saldoElement.textContent = formatarMoeda(Math.abs(saldo));
    
    // Colorir saldo
    const saldoCard = saldoElement.closest('.card');
    if (saldo >= 0) {
        saldoCard.style.background = 'linear-gradient(135deg, #4CAF50 0%, #45a049 100%)';
    } else {
        saldoCard.style.background = 'linear-gradient(135deg, #f44336 0%, #da190b 100%)';
    }
}

// Adicionar transação
async function adicionarTransacao(tipo) {
    const form = document.getElementById('transacaoForm');
    
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const descricao = document.getElementById('descricao').value.trim();
    const valor = parseFloat(document.getElementById('valor').value);
    const data = document.getElementById('data').value;
    const categoriaNome = document.getElementById('categoria').value.trim();
    const observacoes = document.getElementById('observacoes').value.trim();

    // Validar campos
    if (!categoriaNome) {
        mostrarErro('Por favor, digite uma categoria!');
        return;
    }

    try {
        // Buscar ou criar categoria
        let categoria = categorias.find(c => 
            c.nome.toLowerCase() === categoriaNome.toLowerCase()
        );

        // Se categoria não existe, criar
        if (!categoria) {
            console.log(`Criando nova categoria: ${categoriaNome}`);
            
            // Definir cor baseada no tipo
            const cores = {
                'RECEITA': ['#4CAF50', '#8BC34A', '#009688', '#00BCD4', '#4DD0E1'],
                'DESPESA': ['#F44336', '#FF5722', '#FF9800', '#9C27B0', '#E91E63']
            };
            const corAleatoria = cores[tipo][Math.floor(Math.random() * cores[tipo].length)];
            
            const novaCategoria = {
                nome: categoriaNome,
                descricao: `Categoria ${categoriaNome}`,
                tipo: tipo,
                cor: corAleatoria
            };

            const responseCat = await fetch(CATEGORIAS_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(novaCategoria)
            });

            if (!responseCat.ok) {
                throw new Error('Erro ao criar categoria');
            }

            categoria = await responseCat.json();
            categorias.push(categoria);
            
            // Atualizar sugestões
            await carregarCategorias();
        }

        // Criar transação
        const transacao = {
            descricao,
            valor,
            tipo,
            data,
            categoriaId: categoria.id,
            observacoes: observacoes || null
        };

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(transacao)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Erro ao criar transação');
        }

        const novaTransacao = await response.json();
        
        // Limpar formulário
        form.reset();
        setDataAtual();

        // Recarregar dados
        await carregarTransacoes();

        // Mostrar mensagem de sucesso
        const tipoTexto = tipo === 'RECEITA' ? 'Receita' : 'Despesa';
        mostrarSucesso(`${tipoTexto} de ${formatarMoeda(valor)} adicionada com sucesso!`);

    } catch (error) {
        console.error('Erro ao adicionar transação:', error);
        mostrarErro('Erro: ' + error.message);
    }
}

// Deletar transação
async function deletarTransacao(id) {
    if (!confirm('Tem certeza que deseja deletar esta transação?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Erro ao deletar transação');
        }

        await carregarTransacoes();
        mostrarSucesso('Transação deletada com sucesso!');

    } catch (error) {
        console.error('Erro ao deletar:', error);
        mostrarErro('Erro ao deletar transação');
    }
}

// Formatação
function formatarMoeda(valor) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(valor);
}

function formatarData(dataStr) {
    const data = new Date(dataStr + 'T00:00:00');
    return data.toLocaleDateString('pt-BR');
}

// Mensagens
function mostrarSucesso(mensagem) {
    document.getElementById('modalMensagem').textContent = mensagem;
    document.getElementById('modalConfirmacao').style.display = 'flex';
}

function mostrarErro(mensagem) {
    alert('❌ ' + mensagem);
}

