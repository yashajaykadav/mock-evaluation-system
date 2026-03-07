// Admin Dashboard JavaScript

let batches = [];
let technologies = [];
let users = [];
let participants = [];
let evaluators = [];

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    loadUsers();
});

// Navigation
function setupNavigation() {
    const menuItems = document.querySelectorAll('.sidebar-menu a[data-section]');
    menuItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            showSection(section);

            menuItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');

            document.getElementById('pageTitle').textContent =
                item.textContent;
        });
    });
}

function showSection(sectionName) {
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.style.display = 'none';
    });

    const targetSection = document.getElementById(`${sectionName}-section`);
    if (targetSection) {
        targetSection.style.display = 'block';

        // Load data for the section
        switch(sectionName) {
            case 'users':
                loadUsers();
                break;
            case 'batches':
                loadBatches();
                break;
            case 'technologies':
                loadTechnologies();
                break;
            case 'rounds':
                loadBatchesForFilter();
                loadTechnologiesForFilter();
                break;
            case 'participants':
                loadParticipants();
                break;
            case 'assignments':
                loadAssignments();
                break;
            case 'reports':
                loadBatchesForReports();
                loadTechnologiesForReports();
                break;
        }
    }
}

// User Management
async function loadUsers() {
    try {
        const response = await fetch('/admin/api/users');
        users = await response.json();

        const tbody = document.querySelector('#usersTable tbody');
        tbody.innerHTML = '';

        users.forEach(user => {
            const row = `
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td><span class="badge badge-${user.active ? 'success' : 'danger'}">
                        ${user.active ? 'Active' : 'Inactive'}
                    </span></td>
                    <td>
                        <button class="btn btn-small btn-secondary" onclick="editUser(${user.id})">Edit</button>
                        <button class="btn btn-small btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading users:', error);
        showNotification('Error loading users', 'error');
    }
}

function showUserModal(userId = null) {
    const modal = document.getElementById('userModal');
    const form = document.getElementById('userForm');
    const title = document.getElementById('userModalTitle');

    form.reset();

    if (userId) {
        title.textContent = 'Edit User';
        const user = users.find(u => u.id === userId);
        if (user) {
            document.getElementById('userId').value = user.id;
            document.getElementById('userName').value = user.name;
            document.getElementById('userEmail').value = user.email;
            document.getElementById('userRole').value = user.role;
            document.getElementById('userPassword').required = false;
        }
    } else {
        title.textContent = 'Add User';
        document.getElementById('userPassword').required = true;
    }

    openModal('userModal');
}

function editUser(userId) {
    showUserModal(userId);
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await fetch(`/admin/api/users/${userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showNotification('User deleted successfully');
            loadUsers();
        } else {
            showNotification('Error deleting user', 'error');
        }
    } catch (error) {
        console.error('Error deleting user:', error);
        showNotification('Error deleting user', 'error');
    }
}

document.getElementById('userForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const userId = document.getElementById('userId').value;
    const userData = {
        name: document.getElementById('userName').value,
        email: document.getElementById('userEmail').value,
        password: document.getElementById('userPassword').value,
        role: document.getElementById('userRole').value,
        active: true
    };

    try {
        const url = userId ? `/admin/api/users/${userId}` : '/admin/api/users';
        const method = userId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            showNotification(`User ${userId ? 'updated' : 'created'} successfully`);
            closeModal('userModal');
            loadUsers();
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error saving user', 'error');
        }
    } catch (error) {
        console.error('Error saving user:', error);
        showNotification('Error saving user', 'error');
    }
});

// Batch Management
async function loadBatches() {
    try {
        const response = await fetch('/admin/api/batches');
        batches = await response.json();

        const tbody = document.querySelector('#batchesTable tbody');
        tbody.innerHTML = '';

        batches.forEach(batch => {
            const row = `
                <tr>
                    <td>${batch.name}</td>
                    <td>${formatDate(batch.startDate)}</td>
                    <td>${formatDate(batch.endDate)}</td>
                    <td><span class="badge badge-${batch.active ? 'success' : 'danger'}">
                        ${batch.active ? 'Active' : 'Inactive'}
                    </span></td>
                    <td>
                        <button class="btn btn-small btn-secondary" onclick="editBatch(${batch.id})">Edit</button>
                        <button class="btn btn-small btn-danger" onclick="deleteBatch(${batch.id})">Delete</button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading batches:', error);
        showNotification('Error loading batches', 'error');
    }
}

function showBatchModal(batchId = null) {
    const modal = document.getElementById('batchModal');
    const form = document.getElementById('batchForm');
    const title = document.getElementById('batchModalTitle');

    form.reset();

    if (batchId) {
        title.textContent = 'Edit Batch';
        const batch = batches.find(b => b.id === batchId);
        if (batch) {
            document.getElementById('batchId').value = batch.id;
            document.getElementById('batchName').value = batch.name;
            document.getElementById('batchStartDate').value = batch.startDate;
            document.getElementById('batchEndDate').value = batch.endDate;
        }
    } else {
        title.textContent = 'Add Batch';
    }

    openModal('batchModal');
}

function editBatch(batchId) {
    showBatchModal(batchId);
}

async function deleteBatch(batchId) {
    if (!confirm('Are you sure you want to delete this batch?')) return;

    try {
        const response = await fetch(`/admin/api/batches/${batchId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showNotification('Batch deleted successfully');
            loadBatches();
        } else {
            showNotification('Error deleting batch', 'error');
        }
    } catch (error) {
        console.error('Error deleting batch:', error);
        showNotification('Error deleting batch', 'error');
    }
}

document.getElementById('batchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const batchId = document.getElementById('batchId').value;
    const batchData = {
        name: document.getElementById('batchName').value,
        startDate: document.getElementById('batchStartDate').value,
        endDate: document.getElementById('batchEndDate').value,
        active: true
    };

    try {
        const url = batchId ? `/admin/api/batches/${batchId}` : '/admin/api/batches';
        const method = batchId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(batchData)
        });

        if (response.ok) {
            showNotification(`Batch ${batchId ? 'updated' : 'created'} successfully`);
            closeModal('batchModal');
            loadBatches();
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error saving batch', 'error');
        }
    } catch (error) {
        console.error('Error saving batch:', error);
        showNotification('Error saving batch', 'error');
    }
});

// Technology Management (similar pattern to batches)
async function loadTechnologies() {
    try {
        const response = await fetch('/admin/api/technologies');
        technologies = await response.json();

        const tbody = document.querySelector('#technologiesTable tbody');
        tbody.innerHTML = '';

        technologies.forEach(tech => {
            const row = `
                <tr>
                    <td>${tech.name}</td>
                    <td>${tech.description || '-'}</td>
                    <td><span class="badge badge-${tech.active ? 'success' : 'danger'}">
                        ${tech.active ? 'Active' : 'Inactive'}
                    </span></td>
                    <td>
                        <button class="btn btn-small btn-secondary" onclick="editTechnology(${tech.id})">Edit</button>
                        <button class="btn btn-small btn-danger" onclick="deleteTechnology(${tech.id})">Delete</button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading technologies:', error);
        showNotification('Error loading technologies', 'error');
    }
}

function showTechnologyModal(techId = null) {
    const modal = document.getElementById('technologyModal');
    const form = document.getElementById('technologyForm');
    const title = document.getElementById('technologyModalTitle');

    form.reset();

    if (techId) {
        title.textContent = 'Edit Technology';
        const tech = technologies.find(t => t.id === techId);
        if (tech) {
            document.getElementById('technologyId').value = tech.id;
            document.getElementById('technologyName').value = tech.name;
            document.getElementById('technologyDescription').value = tech.description || '';
        }
    } else {
        title.textContent = 'Add Technology';
    }

    openModal('technologyModal');
}

function editTechnology(techId) {
    showTechnologyModal(techId);
}

async function deleteTechnology(techId) {
    if (!confirm('Are you sure you want to delete this technology?')) return;

    try {
        const response = await fetch(`/admin/api/technologies/${techId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showNotification('Technology deleted successfully');
            loadTechnologies();
        } else {
            showNotification('Error deleting technology', 'error');
        }
    } catch (error) {
        console.error('Error deleting technology:', error);
        showNotification('Error deleting technology', 'error');
    }
}

document.getElementById('technologyForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const techId = document.getElementById('technologyId').value;
    const techData = {
        name: document.getElementById('technologyName').value,
        description: document.getElementById('technologyDescription').value,
        active: true
    };

    try {
        const url = techId ? `/admin/api/technologies/${techId}` : '/admin/api/technologies';
        const method = techId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(techData)
        });

        if (response.ok) {
            showNotification(`Technology ${techId ? 'updated' : 'created'} successfully`);
            closeModal('technologyModal');
            loadTechnologies();
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error saving technology', 'error');
        }
    } catch (error) {
        console.error('Error saving technology:', error);
        showNotification('Error saving technology', 'error');
    }
});

// Round Configuration
async function loadBatchesForFilter() {
    if (batches.length === 0) {
        const response = await fetch('/admin/api/batches');
        batches = await response.json();
    }

    const select = document.getElementById('roundBatchFilter');
    select.innerHTML = '<option value="">Select Batch</option>';
    batches.forEach(batch => {
        select.innerHTML += `<option value="${batch.id}">${batch.name}</option>`;
    });
}

async function loadTechnologiesForFilter() {
    if (technologies.length === 0) {
        const response = await fetch('/admin/api/technologies');
        technologies = await response.json();
    }

    const select = document.getElementById('roundTechnologyFilter');
    select.innerHTML = '<option value="">Select Technology</option>';
    technologies.forEach(tech => {
        select.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
    });
}

async function loadRounds() {
    const batchId = document.getElementById('roundBatchFilter').value;
    const techId = document.getElementById('roundTechnologyFilter').value;

    if (!batchId || !techId) {
        document.querySelector('#roundsTable tbody').innerHTML = '';
        return;
    }

    try {
        const response = await fetch(`/admin/api/rounds?batchId=${batchId}&technologyId=${techId}`);
        const rounds = await response.json();

        const tbody = document.querySelector('#roundsTable tbody');
        tbody.innerHTML = '';

        rounds.forEach(round => {
            const batch = batches.find(b => b.id === round.batch.id);
            const tech = technologies.find(t => t.id === round.technology.id);

            const row = `
                <tr>
                    <td>${batch?.name || '-'}</td>
                    <td>${tech?.name || '-'}</td>
                    <td>${round.roundNumber}</td>
                    <td>${round.roundName || '-'}</td>
                    <td>${round.totalRounds || '-'}</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading rounds:', error);
        showNotification('Error loading rounds', 'error');
    }
}

async function showRoundModal() {
    if (batches.length === 0) await loadBatches();
    if (technologies.length === 0) await loadTechnologies();

    const batchSelect = document.getElementById('roundBatch');
    const techSelect = document.getElementById('roundTechnology');

    batchSelect.innerHTML = '<option value="">Select Batch</option>';
    batches.forEach(batch => {
        batchSelect.innerHTML += `<option value="${batch.id}">${batch.name}</option>`;
    });

    techSelect.innerHTML = '<option value="">Select Technology</option>';
    technologies.forEach(tech => {
        techSelect.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
    });

    document.getElementById('roundForm').reset();
    openModal('roundModal');
}

document.getElementById('roundForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const roundData = {
        batch: { id: parseInt(document.getElementById('roundBatch').value) },
        technology: { id: parseInt(document.getElementById('roundTechnology').value) },
        roundNumber: parseInt(document.getElementById('roundNumber').value),
        roundName: document.getElementById('roundName').value,
        totalRounds: parseInt(document.getElementById('totalRounds').value)
    };

    try {
        const response = await fetch('/admin/api/rounds', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(roundData)
        });

        if (response.ok) {
            showNotification('Round created successfully');
            closeModal('roundModal');
            loadRounds();
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error creating round', 'error');
        }
    } catch (error) {
        console.error('Error creating round:', error);
        showNotification('Error creating round', 'error');
    }
});

// Participant Management
async function loadParticipants() {
    try {
        const response = await fetch('/admin/api/participants');
        participants = await response.json();

        const tbody = document.querySelector('#participantsTable tbody');
        tbody.innerHTML = '';

        participants.forEach(participant => {
            const row = `
                <tr>
                    <td>${participant.name}</td>
                    <td>${participant.email}</td>
                    <td>${participant.batch?.name || '-'}</td>
                    <td>${participant.technology?.name || '-'}</td>
                    <td><span class="badge badge-${participant.active ? 'success' : 'danger'}">
                        ${participant.active ? 'Active' : 'Inactive'}
                    </span></td>
                    <td>
                        <button class="btn btn-small btn-secondary" onclick="editParticipant(${participant.id})">Edit</button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading participants:', error);
        showNotification('Error loading participants', 'error');
    }
}

async function showParticipantModal(participantId = null) {
    if (batches.length === 0) await loadBatches();
    if (technologies.length === 0) await loadTechnologies();

    const batchSelect = document.getElementById('participantBatch');
    const techSelect = document.getElementById('participantTechnology');

    batchSelect.innerHTML = '<option value="">Select Batch</option>';
    batches.forEach(batch => {
        batchSelect.innerHTML += `<option value="${batch.id}">${batch.name}</option>`;
    });

    techSelect.innerHTML = '<option value="">Select Technology</option>';
    technologies.forEach(tech => {
        techSelect.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
    });

    const form = document.getElementById('participantForm');
    const title = document.getElementById('participantModalTitle');

    form.reset();

    if (participantId) {
        title.textContent = 'Edit Participant';
        const participant = participants.find(p => p.id === participantId);
        if (participant) {
            document.getElementById('participantId').value = participant.id;
            document.getElementById('participantName').value = participant.name;
            document.getElementById('participantEmail').value = participant.email;
            document.getElementById('participantBatch').value = participant.batch?.id || '';
            document.getElementById('participantTechnology').value = participant.technology?.id || '';
        }
    } else {
        title.textContent = 'Add Participant';
    }

    openModal('participantModal');
}

function editParticipant(participantId) {
    showParticipantModal(participantId);
}

document.getElementById('participantForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const participantId = document.getElementById('participantId').value;
    const participantData = {
        name: document.getElementById('participantName').value,
        email: document.getElementById('participantEmail').value,
        batch: { id: parseInt(document.getElementById('participantBatch').value) },
        technology: { id: parseInt(document.getElementById('participantTechnology').value) },
        active: true
    };

    try {
        const url = participantId ? `/admin/api/participants/${participantId}` : '/admin/api/participants';
        const method = participantId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(participantData)
        });

        if (response.ok) {
            showNotification(`Participant ${participantId ? 'updated' : 'created'} successfully`);
            closeModal('participantModal');
            loadParticipants();
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error saving participant', 'error');
        }
    } catch (error) {
        console.error('Error saving participant:', error);
        showNotification('Error saving participant', 'error');
    }
});

// Evaluation Assignment
async function loadAssignments() {
    try {
        const response = await fetch('/admin/api/evaluations');
        const evaluations = await response.json();

        const tbody = document.querySelector('#assignmentsTable tbody');
        tbody.innerHTML = '';

        evaluations.forEach(evaluation => {
            const row = `
                <tr>
                    <td>${evaluation.participant?.name || '-'}</td>
                    <td>${evaluation.evaluator?.name || '-'}</td>
                    <td>${evaluation.round?.roundName || 'Round ' + evaluation.round?.roundNumber}</td>
                    <td><span class="badge badge-${evaluation.status === 'COMPLETED' ? 'success' : 'warning'}">
                        ${evaluation.status}
                    </span></td>
                    <td>${evaluation.score !== null ? evaluation.score : '-'}</td>
                    <td>${evaluation.evaluatedAt ? formatDateTime(evaluation.evaluatedAt) : '-'}</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading assignments:', error);
        showNotification('Error loading assignments', 'error');
    }
}

async function showAssignmentModal() {
    if (participants.length === 0) await loadParticipants();
    if (batches.length === 0) await loadBatches();
    if (technologies.length === 0) await loadTechnologies();

    const response = await fetch('/admin/api/users/evaluators');
    evaluators = await response.json();

    const participantSelect = document.getElementById('assignParticipant');
    participantSelect.innerHTML = '<option value="">Select Participant</option>';
    participants.forEach(participant => {
        participantSelect.innerHTML += `<option value="${participant.id}">${participant.name}</option>`;
    });

    const evaluatorSelect = document.getElementById('assignEvaluator');
    evaluatorSelect.innerHTML = '<option value="">Select Evaluator</option>';
    evaluators.forEach(evaluator => {
        evaluatorSelect.innerHTML += `<option value="${evaluator.id}">${evaluator.name}</option>`;
    });

    const batchSelect = document.getElementById('assignBatch');
    batchSelect.innerHTML = '<option value="">Select Batch</option>';
    batches.forEach(batch => {
        batchSelect.innerHTML += `<option value="${batch.id}">${batch.name}</option>`;
    });

    const techSelect = document.getElementById('assignTechnology');
    techSelect.innerHTML = '<option value="">Select Technology</option>';
    technologies.forEach(tech => {
        techSelect.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
    });

    document.getElementById('assignmentForm').reset();
    openModal('assignmentModal');
}

async function loadRoundsForAssignment() {
    const batchId = document.getElementById('assignBatch').value;
    const techId = document.getElementById('assignTechnology').value;

    if (!batchId || !techId) {
        document.getElementById('assignRound').innerHTML = '<option value="">Select Round</option>';
        return;
    }

    try {
        const response = await fetch(`/admin/api/rounds?batchId=${batchId}&technologyId=${techId}`);
        const rounds = await response.json();

        const roundSelect = document.getElementById('assignRound');
        roundSelect.innerHTML = '<option value="">Select Round</option>';
        rounds.forEach(round => {
            roundSelect.innerHTML += `<option value="${round.id}">${round.roundName || 'Round ' + round.roundNumber}</option>`;
        });
    } catch (error) {
        console.error('Error loading rounds:', error);
    }
}

document.getElementById('assignmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const assignmentData = {
        participantId: parseInt(document.getElementById('assignParticipant').value),
        evaluatorId: parseInt(document.getElementById('assignEvaluator').value),
        roundId: parseInt(document.getElementById('assignRound').value)
    };

    try {
        const response = await fetch('/admin/api/evaluations/assign', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(assignmentData)
        });

        if (response.ok) {
            showNotification('Evaluation assigned successfully');
            closeModal('assignmentModal');
            loadAssignments();
        } else {
            const error = await response.json();
            showNotification(error.error || 'Error assigning evaluation', 'error');
        }
    } catch (error) {
        console.error('Error assigning evaluation:', error);
        showNotification('Error assigning evaluation', 'error');
    }
});

// Reports
async function loadBatchesForReports() {
    if (batches.length === 0) {
        const response = await fetch('/admin/api/batches');
        batches = await response.json();
    }

    const select = document.getElementById('reportBatch');
    select.innerHTML = '<option value="">Select Batch</option>';
    batches.forEach(batch => {
        select.innerHTML += `<option value="${batch.id}">${batch.name}</option>`;
    });
}

async function loadTechnologiesForReports() {
    if (technologies.length === 0) {
        const response = await fetch('/admin/api/technologies');
        technologies = await response.json();
    }

    const selects = ['reportTechnology', 'roundReportTechnology'];
    selects.forEach(selectId => {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Select Technology</option>';
        technologies.forEach(tech => {
            select.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
        });
    });
}

async function loadParticipantReport() {
    const batchId = document.getElementById('reportBatch').value;
    const techId = document.getElementById('reportTechnology').value;

    if (!batchId || !techId) {
        showNotification('Please select both batch and technology', 'error');
        return;
    }

    try {
        const response = await fetch(`/admin/reports/api/participants?batchId=${batchId}&technologyId=${techId}`);
        const reports = await response.json();

        const container = document.getElementById('participantReportTable');

        if (reports.length === 0) {
            container.innerHTML = '<p>No data available</p>';
            return;
        }

        let html = `
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Participant Name</th>
                        <th>Email</th>
                        <th>Average Score</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
        `;

        reports.forEach(report => {
            html += `
                <tr>
                    <td>${report.participantName}</td>
                    <td>${report.participantEmail}</td>
                    <td>${report.averageScore.toFixed(2)}</td>
                    <td><span class="badge badge-${report.status === 'Completed' ? 'success' : 'warning'}">
                        ${report.status}
                    </span></td>
                </tr>
            `;
        });

        html += '</tbody></table>';
        container.innerHTML = html;
    } catch (error) {
        console.error('Error loading participant report:', error);
        showNotification('Error loading report', 'error');
    }
}

async function loadRoundReport() {
    const techId = document.getElementById('roundReportTechnology').value;

    if (!techId) {
        showNotification('Please select a technology', 'error');
        return;
    }

    try {
        const response = await fetch(`/admin/reports/api/rounds?technologyId=${techId}`);
        const reports = await response.json();

        const container = document.getElementById('roundReportTable');

        if (reports.length === 0) {
            container.innerHTML = '<p>No data available</p>';
            return;
        }

        let html = `
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Round Number</th>
                        <th>Round Name</th>
                        <th>Average Score</th>
                        <th>Completed</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
        `;

        reports.forEach(report => {
            html += `
                <tr>
                    <td>${report.roundNumber}</td>
                    <td>${report.roundName || '-'}</td>
                    <td>${report.averageScore ? report.averageScore.toFixed(2) : '-'}</td>
                    <td>${report.completedEvaluations}</td>
                    <td>${report.totalEvaluations}</td>
                </tr>
            `;
        });

        html += '</tbody></table>';
        container.innerHTML = html;
    } catch (error) {
        console.error('Error loading round report:', error);
        showNotification('Error loading report', 'error');
    }
}

async function exportPdf() {
    const batchId = document.getElementById('reportBatch').value;
    const techId = document.getElementById('reportTechnology').value;

    if (!batchId || !techId) {
        showNotification('Please select both batch and technology', 'error');
        return;
    }

    window.location.href = `/admin/reports/api/export/pdf?batchId=${batchId}&technologyId=${techId}`;
}

async function exportCsv() {
    const batchId = document.getElementById('reportBatch').value;
    const techId = document.getElementById('reportTechnology').value;

    if (!batchId || !techId) {
        showNotification('Please select both batch and technology', 'error');
        return;
    }

    window.location.href = `/admin/reports/api/export/csv?batchId=${batchId}&technologyId=${techId}`;
}