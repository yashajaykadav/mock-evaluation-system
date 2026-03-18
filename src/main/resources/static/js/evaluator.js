// Evaluator Dashboard JavaScript

let evaluations = [];

// Get token from localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Check authentication
function checkAuth() {
    const token = getToken();
    if (!token) {
        window.location.href = '/login';
        return false;
    }
    return true;
}

// API call helper with token
async function apiCall(url, options = {}) {
    const token = getToken();
    
    if (!token) {
        window.location.href = '/login';
        return null;
    }
    
    options.headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`
    };
    
    const response = await fetch(url, options);
    
    // Handle unauthorized responses
    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem('token');
        window.location.href = '/login';
        return null;
    }
    
    return response;
}

document.addEventListener('DOMContentLoaded', () => {
    if (!checkAuth()) return;
    loadEvaluations();
    setupLogout();
});

// Setup logout button
function setupLogout() {
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('token');
            window.location.href = '/login';
        });
    }
}

async function loadEvaluations() {
    try {
        const response = await apiCall('/evaluator/api/evaluations');
        
        if (!response) return;
        
        if (!response.ok) {
            throw new Error('Failed to load evaluations');
        }
        
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

    if (evaluations.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">No evaluations assigned</td></tr>';
        return;
    }

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

function showEvaluationForm(evaluationId) {
    const evaluation = evaluations.find(e => e.id === evaluationId);

    if (!evaluation) return;

    // Reset form state
    document.getElementById('evaluationScore').disabled = false;
    document.getElementById('evaluationComments').disabled = false;
    const submitBtn = document.querySelector('#evaluationForm button[type="submit"]');
    if (submitBtn) submitBtn.style.display = 'block';

    // Fill form
    document.getElementById('evaluationId').value = evaluation.id;
    document.getElementById('evalParticipantName').textContent = evaluation.participant?.name || '-';
    document.getElementById('evalBatch').textContent = evaluation.participant?.batch?.name || '-';
    document.getElementById('evalTechnology').textContent = evaluation.participant?.technology?.name || '-';
    document.getElementById('evalRound').textContent = evaluation.round?.roundName || 'Round ' + evaluation.round?.roundNumber;

    document.getElementById('evaluationScore').value = evaluation.score || '';
    document.getElementById('evaluationComments').value = evaluation.comments || '';

    openModal('evaluationModal');
}

function viewEvaluation(evaluationId) {
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
    if (submitBtn) submitBtn.style.display = 'none';

    openModal('evaluationModal');
}

// Reset form when modal closes
function resetEvaluationForm() {
    document.getElementById('evaluationScore').disabled = false;
    document.getElementById('evaluationComments').disabled = false;
    const submitBtn = document.querySelector('#evaluationForm button[type="submit"]');
    if (submitBtn) submitBtn.style.display = 'block';
    document.getElementById('evaluationForm').reset();
}

document.getElementById('evaluationForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const evaluationId = document.getElementById('evaluationId').value;
    const score = parseFloat(document.getElementById('evaluationScore').value);
    const comments = document.getElementById('evaluationComments').value;

    // Validation
    if (isNaN(score)) {
        showNotification('Please enter a valid score', 'error');
        return;
    }

    if (score < 0 || score > 100) {
        showNotification('Score must be between 0 and 100', 'error');
        return;
    }

    try {
        const response = await apiCall(`/evaluator/api/evaluations/${evaluationId}/submit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ score, comments })
        });

        if (!response) return;

        if (response.ok) {
            showNotification('Evaluation submitted successfully');
            closeModal('evaluationModal');
            resetEvaluationForm();
            loadEvaluations();
        } else {
            const error = await response.json();
            showNotification(error.error || error.message || 'Error submitting evaluation', 'error');
        }
    } catch (error) {
        console.error('Error submitting evaluation:', error);
        showNotification('Error submitting evaluation', 'error');
    }
});

// Close modal event - reset form
document.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal') || e.target.classList.contains('close')) {
        resetEvaluationForm();
    }
});