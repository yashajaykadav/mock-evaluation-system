// Evaluator Dashboard JavaScript

let evaluations = [];

document.addEventListener('DOMContentLoaded', () => {
    loadEvaluations();
});

async function loadEvaluations() {
    try {
        const response = await fetch('/evaluator/api/evaluations');
        evaluations = await response.json();

        updateStats();
        renderEvaluationsTable();
    } catch (error) {
        console.error('Error loading evaluations:', error);
        showNotification('Error loading evaluations', 'error');
    }
}

function updateStats() {
    const total = evaluations.length;
    const completed = evaluations.filter(e => e.status === 'COMPLETED').length;
    const pending = total - completed;

    document.getElementById('totalEvaluations').textContent = total;
    document.getElementById('completedEvaluations').textContent = completed;
    document.getElementById('pendingEvaluations').textContent = pending;
}

function renderEvaluationsTable() {
    const tbody = document.querySelector('#evaluationsTable tbody');
    tbody.innerHTML = '';

    evaluations.forEach(evaluation => {
        const row = `
            <tr>
                <td>${evaluation.participant?.name || '-'}</td>
                <td>${evaluation.participant?.batch?.name || '-'}</td>
                <td>${evaluation.participant?.technology?.name || '-'}</td>
                <td>${evaluation.round?.roundName || 'Round ' + evaluation.round?.roundNumber}</td>
                <td><span class="badge badge-${evaluation.status === 'COMPLETED' ? 'success' : 'warning'}">
                    ${evaluation.status}
                </span></td>
                <td>${evaluation.score !== null ? evaluation.score : '-'}</td>
                <td>
                    ${evaluation.status === 'PENDING' ?
                        `<button class="btn btn-small btn-primary" onclick="showEvaluationForm(${evaluation.id})">Evaluate</button>` :
                        `<button class="btn btn-small btn-secondary" onclick="viewEvaluation(${evaluation.id})">View</button>`
                    }
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
}

async function showEvaluationForm(evaluationId) {
    const evaluation = evaluations.find(e => e.id === evaluationId);

    if (!evaluation) return;

    document.getElementById('evaluationId').value = evaluation.id;
    document.getElementById('evalParticipantName').textContent = evaluation.participant?.name || '-';
    document.getElementById('evalBatch').textContent = evaluation.participant?.batch?.name || '-';
    document.getElementById('evalTechnology').textContent = evaluation.participant?.technology?.name || '-';
    document.getElementById('evalRound').textContent = evaluation.round?.roundName || 'Round ' + evaluation.round?.roundNumber;

    document.getElementById('evaluationScore').value = evaluation.score || '';
    document.getElementById('evaluationComments').value = evaluation.comments || '';

    openModal('evaluationModal');
}

async function viewEvaluation(evaluationId) {
    const evaluation = evaluations.find(e => e.id === evaluationId);

    if (!evaluation) return;

    document.getElementById('evaluationId').value = evaluation.id;
    document.getElementById('evalParticipantName').textContent = evaluation.participant?.name || '-';
    document.getElementById('evalBatch').textContent = evaluation.participant?.batch?.name || '-';
    document.getElementById('evalTechnology').textContent = evaluation.participant?.technology?.name || '-';
    document.getElementById('evalRound').textContent = evaluation.round?.roundName || 'Round ' + evaluation.round?.roundNumber;

    document.getElementById('evaluationScore').value = evaluation.score || '';
    document.getElementById('evaluationScore').disabled = true;
    document.getElementById('evaluationComments').value = evaluation.comments || '';
    document.getElementById('evaluationComments').disabled = true;

    const submitBtn = document.querySelector('#evaluationForm button[type="submit"]');
    submitBtn.style.display = 'none';

    openModal('evaluationModal');
}

document.getElementById('evaluationForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const evaluationId = document.getElementById('evaluationId').value;
    const score = parseFloat(document.getElementById('evaluationScore').value);
    const comments = document.getElementById('evaluationComments').value;

    if (score < 0 || score > 100) {
        showNotification('Score must be between 0 and 100', 'error');
        return;
    }

    try {
        const response = await fetch(`/evaluator/api/evaluations/${evaluationId}/submit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ score, comments })
        });

        if (response.ok) {
            showNotification('Evaluation submitted successfully');
            closeModal('evaluationModal');
            loadEvaluations();

            // Reset form
            document.getElementById('evaluationScore').disabled = false;
            document.getElementById('evaluationComments').disabled = false;
            document.querySelector('#evaluationForm button[type="submit"]').style.display = 'block';
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error submitting evaluation', 'error');
        }
    } catch (error) {
        console.error('Error submitting evaluation:', error);
        showNotification('Error submitting evaluation', 'error');
    }
});