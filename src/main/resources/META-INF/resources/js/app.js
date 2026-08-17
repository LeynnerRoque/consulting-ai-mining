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

        // Mapeando com base no JSON retornado pela sua API
        document.getElementById('resCargo').textContent = data.seniorityDetected || 'Desenvolvedor Java';
        document.getElementById('resTier').textContent = data.seniorityDetected || 'N/A';
        document.getElementById('resScoreText').textContent = (data.matchScore ?? 0) + '%';

        // Preenchendo Pontos de Match
        const matchesList = document.getElementById('resMatches');
        matchesList.innerHTML = '';
        if (data.matchedTechnologies && data.matchedTechnologies.length > 0) {
            data.matchedTechnologies.forEach(item => {
                matchesList.innerHTML += `<li class="flex items-start space-x-2"><i class="fa-solid fa-check text-emerald-400 mt-1 flex-shrink-0"></i><span>${item}</span></li>`;
            });
        } else {
            matchesList.innerHTML = `<li class="flex items-start space-x-2"><i class="fa-solid fa-check text-emerald-400 mt-1 flex-shrink-0"></i><span>Total de tecnologias compatíveis: ${data.technologiesMatchedCount || 0}</span></li>`;
        }

        // Preenchendo Gaps
        const gapsList = document.getElementById('resGaps');
        gapsList.innerHTML = '';
        if (data.gaps && data.gaps.length > 0) {
            data.gaps.forEach(item => {
                gapsList.innerHTML += `<li class="flex items-start space-x-2"><i class="fa-solid fa-xmark text-amber-400 mt-1 flex-shrink-0"></i><span>${item}</span></li>`;
            });
        } else if (data.gapsCount > 0) {
            gapsList.innerHTML = `<li class="text-slate-400 italic">Foram identificados ${data.gapsCount} pontos de melhoria na stack.</li>`;
        } else {
            gapsList.innerHTML = '<li class="text-slate-500 italic">Nenhum gap crítico identificado! Excelente match.</li>';
        }

        // Preenchendo Dicas Estratégicas
        const tipsList = document.getElementById('resTips');
        tipsList.innerHTML = '';
        if (data.recommendations && data.recommendations.length > 0) {
            data.recommendations.forEach(item => {
                tipsList.innerHTML += `<li class="bg-slate-950 p-3 rounded-xl border border-slate-800 flex items-start space-x-2"><i class="fa-solid fa-circle-dot text-cyan-400 mt-1 flex-shrink-0 text-xs"></i><span>${item}</span></li>`;
            });
        } else {
            tipsList.innerHTML = '<li class="bg-slate-950 p-3 rounded-xl border border-slate-800 text-slate-400 italic">Foque em evidenciar sua experiência com arquitetura corporativa e testes automatizados.</li>';
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