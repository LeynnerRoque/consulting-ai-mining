const fileInput = document.getElementById('resume');
const fileNameDisplay = document.getElementById('fileName');
const miningForm = document.getElementById('miningForm');
const loadingState = document.getElementById('loadingState');
const resultDashboard = document.getElementById('resultDashboard');
const resetBtn = document.getElementById('resetBtn');

const aboutModal = document.getElementById('aboutModal');
const openAboutBtn = document.getElementById('openAboutBtn');
const closeAboutBtn = document.getElementById('closeAboutBtn');
const closeAboutModalBtn = document.getElementById('closeAboutModalBtn');

fileInput.addEventListener('change', (e) => {
    if (e.target.files.length > 0) {
        fileNameDisplay.textContent = e.target.files[0].name;
        fileNameDisplay.classList.add('text-indigo-400', 'font-bold');
    }
});

miningForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(miningForm);

    miningForm.parentElement.classList.add('hidden');
    loadingState.classList.remove('hidden');
    resultDashboard.classList.add('hidden');

    try {
        const response = await fetch('/api/mining/analyze', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || 'Erro no servidor ao processar análise.');
        }

        const data = await response.json();

        // Mapeando com base no seu DTO (AnaliseVagaDTO)
        document.getElementById('resCargo').textContent = data.cargo_vaga || 'Desenvolvedor';
        document.getElementById('resTier').textContent = data.classificacao_tier || 'N/A';
        document.getElementById('resScoreText').textContent = (data.score_compatibilidade ?? 0) + '%';

        // Preenchendo Pontos de Match (pontos_match_forte)
        const matchesList = document.getElementById('resMatches');
        matchesList.innerHTML = '';
        if (data.pontos_match_forte && data.pontos_match_forte.length > 0) {
            data.pontos_match_forte.forEach(item => {
                matchesList.innerHTML += `<li class="flex items-start space-x-2"><i class="fa-solid fa-check text-emerald-400 mt-1 flex-shrink-0"></i><span>${item}</span></li>`;
            });
        }

        // Preenchendo Gaps (gaps_identificados)
        const gapsList = document.getElementById('resGaps');
        gapsList.innerHTML = '';
        if (data.gaps_identificados && data.gaps_identificados.length > 0) {
            data.gaps_identificados.forEach(item => {
                gapsList.innerHTML += `<li class="flex items-start space-x-2"><i class="fa-solid fa-xmark text-amber-400 mt-1 flex-shrink-0"></i><span>${item}</span></li>`;
            });
        } else {
            gapsList.innerHTML = '<li class="text-slate-500 italic">Nenhum gap crítico identificado!</li>';
        }

        // Preenchendo Dicas Estratégicas (dicas_customizacao)
        const tipsList = document.getElementById('resTips');
        tipsList.innerHTML = '';
        if (data.dicas_customizacao && data.dicas_customizacao.length > 0) {
            data.dicas_customizacao.forEach(item => {
                tipsList.innerHTML += `<li class="bg-slate-950 p-3 rounded-xl border border-slate-800 flex items-start space-x-2"><i class="fa-solid fa-circle-dot text-cyan-400 mt-1 flex-shrink-0 text-xs"></i><span>${item}</span></li>`;
            });
        }

        loadingState.classList.add('hidden');
        resultDashboard.classList.remove('hidden');

    } catch (error) {
        alert('Erro ao processar a requisição:\n' + error.message);
        loadingState.classList.add('hidden');
        miningForm.parentElement.classList.remove('hidden');
    }
});

resetBtn.addEventListener('click', () => {
    miningForm.reset();
    fileNameDisplay.textContent = 'Arraste o PDF ou clique para selecionar';
    fileNameDisplay.classList.remove('text-indigo-400', 'font-bold');
    resultDashboard.classList.add('hidden');
    miningForm.parentElement.classList.remove('hidden');
});

if (openAboutBtn && aboutModal) {
    openAboutBtn.addEventListener('click', () => {
        aboutModal.classList.remove('hidden');
    });

    const closeModal = () => aboutModal.classList.add('hidden');

    closeAboutBtn.addEventListener('click', closeModal);
    closeAboutModalBtn.addEventListener('click', closeModal);

    // Fechar ao clicar fora do modal (no backdrop)
    aboutModal.addEventListener('click', (e) => {
        if (e.target === aboutModal) {
            closeModal();
        }
    });
}