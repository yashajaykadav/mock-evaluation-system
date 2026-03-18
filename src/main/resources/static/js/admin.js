// Admin Dashboard JavaScript

let batches = [];
let technologies = [];
let users = [];
let participants = [];
let evaluators = [];
let rounds = [];

// ==================== AUTH HELPERS ====================

function getToken() {
    return localStorage.getItem('token');
}

function checkAuth() {
    const token = getToken();
    if (!token) {
        window.location.href = '/login';
        return false;
    }
    return true;
}

async function apiCall(url, options = {}) {
    const token = getToken();

    if (!token) {
        window.location.href = '/login';
        return null;
    }

    options.headers = {
        ...options.headers,
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    try {
        const response = await fetch(url, options);

        if (response.status === 401 || response.status === 403) {
            console.error('Unauthorized - redirecting to login');
            localStorage.removeItem('token');
            window.location.href = '/login';
            return null;
        }

        return response;
    } catch (error) {
        console.error('API call failed:', error);
        throw error;
    }
}

// ==================== INITIALIZE ====================

document.addEventListener('DOMContentLoaded', () => {
    if (!checkAuth()) return;

    setupNavigation();
    setupLogout();
    loadDashboardData();
});

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

// ==================== NAVIGATION ====================

function setupNavigation() {
    const menuItems = document.querySelectorAll('.sidebar-menu a[data-section]');
    menuItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            showSection(section);

            menuItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');

            document.getElementById('pageTitle').textContent = item.textContent.trim();
        });
    });
}

function showSection(sectionName) {
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.style.display = 'none';
        section.classList.remove('active');
    });

    const targetSection = document.getElementById(`${sectionName}-section`);
    if (targetSection) {
        targetSection.style.display = 'block';
        targetSection.classList.add('active');

        switch (sectionName) {
            case 'dashboard':
                loadDashboardData();
                break;
            case 'users':
                loadUsers();
                break;
            case 'batches':
                loadBatches();
                break;
            case 'technologies':
                loadTechnologies();
                break;
            case 'participants':
                loadParticipants();
                break;
            case 'rounds':
                loadRoundsSection();
                break;
            case 'assignments':
                loadAssignments();
                break;
            case 'reports':
                loadReportsSection();
                break;
        }
    }
}

// ==================== DASHBOARD ====================

async function loadDashboardData() {
    try {
        const response = await apiCall('/admin/api/dashboard/summary');

        if (!response || !response.ok) {
            console.error('Failed to load dashboard summary');
            // Fallback: load counts individually
            await loadDashboardFallback();
            return;
        }

        const data = await response.json();

        document.getElementById('statBatches').textContent = data.activeBatches || data.batches || 0;
        document.getElementById('statTechnologies').textContent = data.technologies || 0;
        document.getElementById('statParticipants').textContent = data.participants || 0;
        document.getElementById('statEvaluators').textContent = data.evaluators || 0;

    } catch (error) {
        console.error('Error loading dashboard:', error);
        await loadDashboardFallback();
    }
}

async function loadDashboardFallback() {
    try {
        // Load batches count
        const batchRes = await apiCall('/admin/api/batches');
        if (batchRes && batchRes.ok) {
            const batchData = await batchRes.json();
            batches = batchData;
            const activeBatches = batchData.filter(b => b.active !== false).length;
            document.getElementById('statBatches').textContent = activeBatches;
        }

        // Load technologies count
        const techRes = await apiCall('/admin/api/technologies');
        if (techRes && techRes.ok) {
            const techData = await techRes.json();
            technologies = techData;
            document.getElementById('statTechnologies').textContent = techData.length;
        }

        // Load participants count
        const partRes = await apiCall('/admin/api/participants');
        if (partRes && partRes.ok) {
            const partData = await partRes.json();
            participants = partData;
            document.getElementById('statParticipants').textContent = partData.length;
        }

        // Load evaluators count
        const evalRes = await apiCall('/admin/api/users/evaluators');
        if (evalRes && evalRes.ok) {
            const evalData = await evalRes.json();
            evaluators = evalData;
            document.getElementById('statEvaluators').textContent = evalData.length;
        }
    } catch (error) {
        console.error('Dashboard fallback error:', error);
    }
}

// ==================== USERS ====================

async function loadUsers() {
    try {
        const response = await apiCall('/admin/api/users');

        if (!response || !response.ok) {
            throw new Error('Failed to load users');
        }

        users = await response.json();

        const tbody = document.querySelector('#usersTable tbody');
        tbody.innerHTML = '';

        if (users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">No users found</td></tr>';
            return;
        }

        users.forEach(user => {
            const row = `
                <tr>
                    <td>${user.name || '-'}</td>
                    <td>${user.email || '-'}</td>
                    <td>${user.role || '-'}</td>
                    <td><span class="badge badge-${user.active !== false ? 'success' : 'danger'}">
                        ${user.active !== false ? 'Active' : 'Inactive'}
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

function editUser(userId) {
    const user = users.find(u => u.id === userId);
    if (!user) return;

    document.getElementById('userId').value = user.id;
    document.getElementById('userName2').value = user.name || '';
    document.getElementById('userEmail').value = user.email || '';
    document.getElementById('userRole').value = user.role || 'EVALUATOR';
    document.getElementById('userPassword').value = '';
    document.getElementById('userPassword').required = false;
    document.getElementById('userModalTitle').textContent = 'Edit User';

    showModal('userModal');
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await apiCall(`/admin/api/users/${userId}`, {
            method: 'DELETE'
        });

        if (response && response.ok) {
            showNotification('User deleted successfully', 'success');
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
        name: document.getElementById('userName2').value,
        email: document.getElementById('userEmail').value,
        role: document.getElementById('userRole').value,
        active: true
    };

    const password = document.getElementById('userPassword').value;
    if (password) {
        userData.password = password;
    }

    try {
        const url = userId ? `/admin/api/users/${userId}` : '/admin/api/users';
        const method = userId ? 'PUT' : 'POST';

        const response = await apiCall(url, {
            method: method,
            body: JSON.stringify(userData)
        });

        if (response && response.ok) {
            showNotification(`User ${userId ? 'updated' : 'created'} successfully`, 'success');
            hideModal('userModal');
            loadUsers();
            document.getElementById('userForm').reset();
            document.getElementById('userId').value = '';
        } else {
            const error = await response.json();
            showNotification(error.message || error.error || 'Error saving user', 'error');
        }
    } catch (error) {
        console.error('Error saving user:', error);
        showNotification('Error saving user', 'error');
    }
});

// ==================== BATCHES ====================

async function loadBatches() {
    try {
        const response = await apiCall('/admin/api/batches');

        if (!response || !response.ok) {
            throw new Error('Failed to load batches');
        }

        batches = await response.json();

        const tbody = document.querySelector('#batchesTable tbody');
        tbody.innerHTML = '';

        if (batches.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">No batches found</td></tr>';
            return;
        }

        batches.forEach(batch => {
            const row = `
                <tr>
                    <td>${batch.name || '-'}</td>
                    <td>${formatDate(batch.startDate)}</td>
                    <td>${formatDate(batch.endDate)}</td>
                    <td><span class="badge badge-${batch.active !== false ? 'success' : 'danger'}">
                        ${batch.active !== false ? 'Active' : 'Inactive'}
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

function editBatch(batchId) {
    const batch = batches.find(b => b.id === batchId);
    if (!batch) return;

    document.getElementById('batchId').value = batch.id;
    document.getElementById('batchName').value = batch.name || '';
    document.getElementById('batchStartDate').value = batch.startDate || '';
    document.getElementById('batchEndDate').value = batch.endDate || '';
    document.getElementById('batchDescription').value = batch.description || '';
    document.getElementById('batchModalTitle').textContent = 'Edit Batch';

    showModal('batchModal');
}

async function deleteBatch(batchId) {
    if (!confirm('Are you sure you want to delete this batch?')) return;

    try {
        const response = await apiCall(`/admin/api/batches/${batchId}`, {
            method: 'DELETE'
        });

        if (response && response.ok) {
            showNotification('Batch deleted successfully', 'success');
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
        description: document.getElementById('batchDescription').value,
        active: true
    };

    try {
        const url = batchId ? `/admin/api/batches/${batchId}` : '/admin/api/batches';
        const method = batchId ? 'PUT' : 'POST';

        const response = await apiCall(url, {
            method: method,
            body: JSON.stringify(batchData)
        });

        if (response && response.ok) {
            showNotification(`Batch ${batchId ? 'updated' : 'created'} successfully`, 'success');
            hideModal('batchModal');
            loadBatches();
            document.getElementById('batchForm').reset();
            document.getElementById('batchId').value = '';
        } else {
            const error = await response.json();
            showNotification(error.message || error.error || 'Error saving batch', 'error');
        }
    } catch (error) {
        console.error('Error saving batch:', error);
        showNotification('Error saving batch', 'error');
    }
});

// ==================== TECHNOLOGIES ====================

async function loadTechnologies() {
    try {
        const response = await apiCall('/admin/api/technologies');

        if (!response || !response.ok) {
            throw new Error('Failed to load technologies');
        }

        technologies = await response.json();

        const tbody = document.querySelector('#technologiesTable tbody');
        tbody.innerHTML = '';

        if (technologies.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">No technologies found</td></tr>';
            return;
        }

        technologies.forEach(tech => {
            const row = `
                <tr>
                    <td>${tech.name || '-'}</td>
                    <td>${tech.description || '-'}</td>
                    <td><span class="badge badge-${tech.active !== false ? 'success' : 'danger'}">
                        ${tech.active !== false ? 'Active' : 'Inactive'}
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

function editTechnology(techId) {
    const tech = technologies.find(t => t.id === techId);
    if (!tech) return;

    document.getElementById('technologyId').value = tech.id;
    document.getElementById('technologyName').value = tech.name || '';
    document.getElementById('technologyDescription').value = tech.description || '';
    document.getElementById('technologyModalTitle').textContent = 'Edit Technology';

    showModal('technologyModal');
}

async function deleteTechnology(techId) {
    if (!confirm('Are you sure you want to delete this technology?')) return;

    try {
        const response = await apiCall(`/admin/api/technologies/${techId}`, {
            method: 'DELETE'
        });

        if (response && response.ok) {
            showNotification('Technology deleted successfully', 'success');
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

        const response = await apiCall(url, {
            method: method,
            body: JSON.stringify(techData)
        });

        if (response && response.ok) {
            showNotification(`Technology ${techId ? 'updated' : 'created'} successfully`, 'success');
            hideModal('technologyModal');
            loadTechnologies();
            document.getElementById('technologyForm').reset();
            document.getElementById('technologyId').value = '';
        } else {
            const error = await response.json();
            showNotification(error.message || error.error || 'Error saving technology', 'error');
        }
    } catch (error) {
        console.error('Error saving technology:', error);
        showNotification('Error saving technology', 'error');
    }
});

// ==================== PARTICIPANTS ====================

async function loadParticipants() {
    try {
        // Load batches and technologies first for dropdowns
        if (batches.length === 0) {
            const batchRes = await apiCall('/admin/api/batches');
            if (batchRes && batchRes.ok) batches = await batchRes.json();
        }
        if (technologies.length === 0) {
            const techRes = await apiCall('/admin/api/technologies');
            if (techRes && techRes.ok) technologies = await techRes.json();
        }

        const response = await apiCall('/admin/api/participants');

        if (!response || !response.ok) {
            throw new Error('Failed to load participants');
        }

        participants = await response.json();

        const tbody = document.querySelector('#participantsTable tbody');
        tbody.innerHTML = '';

        if (participants.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">No participants found</td></tr>';
            return;
        }

        participants.forEach(participant => {
            const row = `
                <tr>
                    <td>${participant.name || '-'}</td>
                    <td>${participant.email || '-'}</td>
                    <td>${participant.batch?.name || '-'}</td>
                    <td>${participant.technology?.name || '-'}</td>
                    <td>
                        <button class="btn btn-small btn-secondary" onclick="editParticipant(${participant.id})">Edit</button>
                        <button class="btn btn-small btn-danger" onclick="deleteParticipant(${participant.id})">Delete</button>
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

function editParticipant(participantId) {
    const participant = participants.find(p => p.id === participantId);
    if (!participant) return;

    // Populate dropdowns
    populateParticipantDropdowns();

    document.getElementById('participantId').value = participant.id;
    document.getElementById('participantName').value = participant.name || '';
    document.getElementById('participantEmail').value = participant.email || '';
    document.getElementById('participantPhone').value = participant.phone || '';
    document.getElementById('participantBatch').value = participant.batch?.id || '';
    document.getElementById('participantTechnology').value = participant.technology?.id || '';
    document.getElementById('participantModalTitle').textContent = 'Edit Participant';

    showModal('participantModal');
}

async function deleteParticipant(participantId) {
    if (!confirm('Are you sure you want to delete this participant?')) return;

    try {
        const response = await apiCall(`/admin/api/participants/${participantId}`, {
            method: 'DELETE'
        });

        if (response && response.ok) {
            showNotification('Participant deleted successfully', 'success');
            loadParticipants();
        } else {
            showNotification('Error deleting participant', 'error');
        }
    } catch (error) {
        console.error('Error deleting participant:', error);
        showNotification('Error deleting participant', 'error');
    }
}

function populateParticipantDropdowns() {
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
}

document.getElementById('participantForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const participantId = document.getElementById('participantId').value;
    const participantData = {
        name: document.getElementById('participantName').value,
        email: document.getElementById('participantEmail').value,
        phone: document.getElementById('participantPhone').value,
        batch: { id: parseInt(document.getElementById('participantBatch').value) },
        technology: { id: parseInt(document.getElementById('participantTechnology').value) },
        active: true
    };

    try {
        const url = participantId ? `/admin/api/participants/${participantId}` : '/admin/api/participants';
        const method = participantId ? 'PUT' : 'POST';

        const response = await apiCall(url, {
            method: method,
            body: JSON.stringify(participantData)
        });

        if (response && response.ok) {
            showNotification(`Participant ${participantId ? 'updated' : 'created'} successfully`, 'success');
            hideModal('participantModal');
            loadParticipants();
            document.getElementById('participantForm').reset();
            document.getElementById('participantId').value = '';
        } else {
            const error = await response.json();
            showNotification(error.message || error.error || 'Error saving participant', 'error');
        }
    } catch (error) {
        console.error('Error saving participant:', error);
        showNotification('Error saving participant', 'error');
    }
});

// ==================== ROUNDS ====================

async function loadRoundsSection() {
    // Load technologies for filter
    if (technologies.length === 0) {
        const techRes = await apiCall('/admin/api/technologies');
        if (techRes && techRes.ok) technologies = await techRes.json();
    }

    const techSelect = document.getElementById('roundTechnology');
    if (techSelect) {
        techSelect.innerHTML = '<option value="">Select Technology</option>';
        technologies.forEach(tech => {
            techSelect.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
        });
    }
}

async function loadRounds() {
    const techId = document.getElementById('roundTechnology')?.value;

    const tbody = document.querySelector('#roundsTable tbody');

    if (!techId) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">Select a technology to view rounds</td></tr>';
        return;
    }

    try {
        const response = await apiCall(`/admin/api/rounds?technologyId=${techId}`);

        if (!response || !response.ok) {
            throw new Error('Failed to load rounds');
        }

        rounds = await response.json();
        tbody.innerHTML = '';

        if (rounds.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">No rounds found</td></tr>';
            return;
        }

        rounds.forEach(round => {
            const row = `
                <tr>
                    <td>${round.batch?.name || '-'}</td>
                    <td>${round.technology?.name || '-'}</td>
                    <td>${round.roundNumber || round.roundOrder || '-'}</td>
                    <td>${round.roundName || '-'}</td>
                    <td>
                        <button class="btn btn-small btn-danger" onclick="deleteRound(${round.id})">Delete</button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading rounds:', error);
        showNotification('Error loading rounds', 'error');
    }
}

async function deleteRound(roundId) {
    if (!confirm('Are you sure you want to delete this round?')) return;

    try {
        const response = await apiCall(`/admin/api/rounds/${roundId}`, {
            method: 'DELETE'
        });

        if (response && response.ok) {
            showNotification('Round deleted successfully', 'success');
            loadRounds();
        } else {
            showNotification('Error deleting round', 'error');
        }
    } catch (error) {
        console.error('Error deleting round:', error);
        showNotification('Error deleting round', 'error');
    }
}

document.getElementById('roundForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const roundData = {
        technology: { id: parseInt(document.getElementById('roundTechnology').value) },
        roundNumber: parseInt(document.getElementById('roundNumber').value),
        roundName: document.getElementById('roundName').value,
        description: document.getElementById('roundDescription').value
    };

    try {
        const response = await apiCall('/admin/api/rounds', {
            method: 'POST',
            body: JSON.stringify(roundData)
        });

        if (response && response.ok) {
            showNotification('Round created successfully', 'success');
            hideModal('roundModal');
            loadRounds();
            document.getElementById('roundForm').reset();
        } else {
            const error = await response.json();
            showNotification(error.message || error.error || 'Error creating round', 'error');
        }
    } catch (error) {
        console.error('Error creating round:', error);
        showNotification('Error creating round', 'error');
    }
});

// ==================== ASSIGNMENTS ====================

async function loadAssignments() {
    try {
        const response = await apiCall('/admin/api/evaluations');

        if (!response || !response.ok) {
            throw new Error('Failed to load assignments');
        }

        const assignments = await response.json();

        const tbody = document.querySelector('#assignmentsTable tbody');
        tbody.innerHTML = '';

        if (assignments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">No assignments found</td></tr>';
            return;
        }

        assignments.forEach(assignment => {
            const row = `
                <tr>
                    <td>${assignment.participant?.name || '-'}</td>
                    <td>${assignment.evaluator?.name || '-'}</td>
                    <td>${assignment.round?.roundName || 'Round ' + (assignment.round?.roundNumber || '-')}</td>
                    <td><span class="badge badge-${assignment.status === 'COMPLETED' ? 'success' : 'warning'}">
                        ${assignment.status || 'PENDING'}
                    </span></td>
                    <td>${assignment.score !== null && assignment.score !== undefined ? assignment.score : '-'}</td>
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
    // Load required data
    if (participants.length === 0) await loadParticipants();
    if (technologies.length === 0) {
        const techRes = await apiCall('/admin/api/technologies');
        if (techRes && techRes.ok) technologies = await techRes.json();
    }

    const evalRes = await apiCall('/admin/api/users/evaluators');
    if (evalRes && evalRes.ok) evaluators = await evalRes.json();

    // Populate dropdowns
    const participantSelect = document.getElementById('assignParticipant');
    participantSelect.innerHTML = '<option value="">Select Participant</option>';
    participants.forEach(p => {
        participantSelect.innerHTML += `<option value="${p.id}">${p.name}</option>`;
    });

    const evaluatorSelect = document.getElementById('assignEvaluator');
    evaluatorSelect.innerHTML = '<option value="">Select Evaluator</option>';
    evaluators.forEach(e => {
        evaluatorSelect.innerHTML += `<option value="${e.id}">${e.name}</option>`;
    });

    const roundSelect = document.getElementById('assignRound');
    roundSelect.innerHTML = '<option value="">Select Round</option>';

    // Load all rounds
    const roundRes = await apiCall('/admin/api/rounds');
    if (roundRes && roundRes.ok) {
        const allRounds = await roundRes.json();
        allRounds.forEach(r => {
            roundSelect.innerHTML += `<option value="${r.id}">${r.roundName || 'Round ' + r.roundNumber} (${r.technology?.name || ''})</option>`;
        });
    }

    showModal('assignmentModal');
}

document.getElementById('assignmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const assignmentData = {
        participantId: parseInt(document.getElementById('assignParticipant').value),
        evaluatorId: parseInt(document.getElementById('assignEvaluator').value),
        roundId: parseInt(document.getElementById('assignRound').value)
    };

    try {
        const response = await apiCall('/admin/api/evaluations/assign', {
            method: 'POST',
            body: JSON.stringify(assignmentData)
        });

        if (response && response.ok) {
            showNotification('Evaluation assigned successfully', 'success');
            hideModal('assignmentModal');
            loadAssignments();
            document.getElementById('assignmentForm').reset();
        } else {
            const error = await response.json();
            showNotification(error.message || error.error || 'Error assigning evaluation', 'error');
        }
    } catch (error) {
        console.error('Error assigning evaluation:', error);
        showNotification('Error assigning evaluation', 'error');
    }
});

// ==================== REPORTS ====================

async function loadReportsSection() {
    if (batches.length === 0) {
        const batchRes = await apiCall('/admin/api/batches');
        if (batchRes && batchRes.ok) batches = await batchRes.json();
    }
    if (technologies.length === 0) {
        const techRes = await apiCall('/admin/api/technologies');
        if (techRes && techRes.ok) technologies = await techRes.json();
    }

    const batchSelect = document.getElementById('reportBatch');
    batchSelect.innerHTML = '<option value="">Select Batch</option>';
    batches.forEach(batch => {
        batchSelect.innerHTML += `<option value="${batch.id}">${batch.name}</option>`;
    });

    const techSelect = document.getElementById('reportTechnology');
    techSelect.innerHTML = '<option value="">Select Technology</option>';
    technologies.forEach(tech => {
        techSelect.innerHTML += `<option value="${tech.id}">${tech.name}</option>`;
    });
}

async function loadReport() {
    const batchId = document.getElementById('reportBatch').value;
    const techId = document.getElementById('reportTechnology').value;

    if (!batchId || !techId) {
        showNotification('Please select both batch and technology', 'error');
        return;
    }

    try {
        const response = await apiCall(`/admin/api/reports/participants?batchId=${batchId}&technologyId=${techId}`);

        if (!response || !response.ok) {
            throw new Error('Failed to load report');
        }

        const reports = await response.json();
        const container = document.getElementById('reportContent');

        if (reports.length === 0) {
            container.innerHTML = '<p style="text-align: center; padding: 20px;">No data available for the selected filters</p>';
            return;
        }

        let html = `
            <table class="data-table" style="width: 100%; margin-top: 20px;">
                <thead>
                    <tr>
                        <th>Participant Name</th>
                        <th>Email</th>
                        <th>Completed Rounds</th>
                        <th>Total Rounds</th>
                        <th>Average Score</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
        `;

        reports.forEach(report => {
            html += `
                <tr>
                    <td>${report.participantName || '-'}</td>
                    <td>${report.participantEmail || '-'}</td>
                    <td>${report.completedRounds || 0}</td>
                    <td>${report.totalRounds || 0}</td>
                    <td>${report.averageScore ? report.averageScore.toFixed(2) : '0.00'}</td>
                    <td><span class="badge badge-${report.status === 'Completed' ? 'success' : 'warning'}">
                        ${report.status || 'In Progress'}
                    </span></td>
                </tr>
            `;
        });

        html += '</tbody></table>';
        container.innerHTML = html;

        showNotification('Report generated successfully', 'success');
    } catch (error) {
        console.error('Error loading report:', error);
        showNotification('Error generating report', 'error');
    }
}

function exportPDF() {
    showNotification('PDF export not available. Please use CSV export.', 'warning');
}

function exportCSV() {
    const batchId = document.getElementById('reportBatch').value;
    const techId = document.getElementById('reportTechnology').value;

    if (!batchId || !techId) {
        showNotification('Please select both batch and technology', 'error');
        return;
    }

    // Direct download with token in URL (alternative approach)
    window.location.href = `/admin/api/reports/export/csv?batchId=${batchId}&technologyId=${techId}`;
}

// ==================== UTILITIES ====================

function formatDate(dateStr) {
    if (!dateStr) return '-';
    try {
        const date = new Date(dateStr);
        return date.toLocaleDateString();
    } catch (e) {
        return dateStr;
    }
}

function formatDateTime(dateStr) {
    if (!dateStr) return '-';
    try {
        const date = new Date(dateStr);
        return date.toLocaleString();
    } catch (e) {
        return dateStr;
    }
}

// ==================== MODAL FUNCTIONS ====================

function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'flex';
    }
}


function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'none';
    }
}

function openModal(modalId) {
    showModal(modalId);
}

function closeModal(modalId) {
    hideModal(modalId);
}

// Close modal when clicking outside
window.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
    }
});

// ==================== NOTIFICATION ====================

function showNotification(message, type = 'success') {
    const toast = document.getElementById('toast');
    if (toast) {
        toast.textContent = message;
        toast.className = `toast toast-${type}`;
        toast.style.display = 'block';
        toast.style.opacity = '1';

        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => {
                toast.style.display = 'none';
            }, 300);
        }, 3000);
    } else {
        console.log(`[${type.toUpperCase()}] ${message}`);
    }
}