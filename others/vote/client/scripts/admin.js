import * as msg from './messageConstants.js';
import { displayMessage } from './tools.js';

const socket = io();
let currentVote = {
    subject: '',
    votes: new Map()
};

const init = () => {
    console.log('Connected as admin');
    socket.emit(msg.ADMIN);
    initMessageListener();
    initButtons();
}

const initMessageListener = () => {
    socket.on(msg.ADMIN_ACCEPTED, adminAccepted);
    socket.on(msg.ADMIN_REFUSED, adminRefused);
    socket.on(msg.NEW_VOTE, handleNewVote);
    socket.on(msg.BYE, () => console.log('Disconnected from server'));
}

const initButtons = () => {
    const startBtn = document.getElementById('start');
    const endBtn = document.getElementById('end');
    const sujetInput = document.getElementById('sujet');

    sujetInput.addEventListener('input', (e) => {
        startBtn.disabled = !e.target.value.trim();
    });

    startBtn.addEventListener('click', startVote);
    endBtn.addEventListener('click', endVote);
}

const adminAccepted = () => {
    document.getElementById('admin').style.display = 'block';
    updateMessage("Connecté en tant qu'administrateur");
}

socket.on(msg.VOTANT, () => {
    const participantCount = document.getElementById('participant');
    const currentCount = parseInt(participantCount.textContent) || 0;
    participantCount.textContent = currentCount + 1;
});

socket.on('voter_disconnect', () => {
    const participantCount = document.getElementById('participant');
    const currentCount = parseInt(participantCount.textContent) || 0;
    if (currentCount > 0) {
        participantCount.textContent = (currentCount - 1).toString();
    }
});

const startVote = () => {
    const subject = document.getElementById('sujet').value.trim();
    if (!subject) return;
    
    currentVote.subject = subject;
    currentVote.votes.clear();
    
    document.getElementById('end').disabled = false;
    document.getElementById('start').disabled = true;
    document.getElementById('sujet').disabled = true;
    
    socket.emit(msg.START_VOTE, subject);
    updateVoteCounts();
    document.getElementById('votes_count').textContent = '0';
  }
  
  const endVote = () => {
      socket.emit(msg.END_VOTE);
      document.getElementById('end').disabled = true;
      document.getElementById('start').disabled = false;
      document.getElementById('sujet').disabled = false;
      document.getElementById('sujet').value = '';
      
      // Réinitialiser tous les compteurs de vote
      document.getElementById('votes_count').textContent = '0';
      document.getElementById('vote_pour').textContent = '0';
      document.getElementById('vote_contre').textContent = '0';
      document.getElementById('vote_nppv').textContent = '0';
      document.getElementById('vote_abst').textContent = '0';
      
      // Vider la Map des votes
      currentVote.votes.clear();
  }

const handleNewVote = (voterId, vote) => {
    currentVote.votes.set(voterId, vote);
    updateVoteCounts();
}

const updateVoteCounts = () => {
    const counts = {
        'pour': 0,
        'contre': 0,
        'nppv': 0,
        'abst': 0
    };

    currentVote.votes.forEach(vote => {
        // Handle abstention votes correctly
        const voteType = vote === 'abstention' ? 'abst' : vote;
        if (counts.hasOwnProperty(voteType)) {
            counts[voteType]++;
        }
    });

    // Update individual vote counts
    Object.entries(counts).forEach(([type, count]) => {
        const element = document.getElementById(`vote_${type}`);
        if (element) {
            element.textContent = count;
        }
    });

    // Update total votes count
    const votesCountElement = document.getElementById('votes_count');
    if (votesCountElement) {
        votesCountElement.textContent = currentVote.votes.size;
    }
}

const updateMessage = (message) => {
    document.getElementById('message').textContent = message;
}

const displayResults = (results) => {
    const voteCounts = {
        pour: 0,
        contre: 0,
        nppv: 0,
        abstention: 0
    };

    results.forEach(([_, vote]) => {
        voteCounts[vote]++;
    });

    const resultDiv = document.createElement('div');
    resultDiv.innerHTML = `
        <div class="mt-4">
            <h3 class="text-xl font-semibold mb-2">Résultats finaux:</h3>
            <div class="grid grid-cols-4 gap-4">
                <div class="bg-blue-100 p-4 rounded">
                    <div class="text-blue-700 font-bold">POUR</div>
                    <div class="text-2xl">${voteCounts.pour}</div>
                </div>
                <div class="bg-red-100 p-4 rounded">
                    <div class="text-red-700 font-bold">CONTRE</div>
                    <div class="text-2xl">${voteCounts.contre}</div>
                </div>
                <div class="bg-yellow-100 p-4 rounded">
                    <div class="text-yellow-700 font-bold">NPPV</div>
                    <div class="text-2xl">${voteCounts.nppv}</div>
                </div>
                <div class="bg-gray-100 p-4 rounded">
                    <div class="text-gray-700 font-bold">ABSTENTION</div>
                    <div class="text-2xl">${voteCounts.abstention}</div>
                </div>
            </div>
            <div class="mt-4">
                <div class="text-sm text-gray-600">
                    Total des votes: ${results.length}
                </div>
            </div>
        </div>
    `;
    
    document.getElementById('resultats').innerHTML = '';
    document.getElementById('resultats').appendChild(resultDiv);
}

const adminRefused = () => {
    // Stocker le message dans sessionStorage pour l'afficher sur la page d'accueil
    sessionStorage.setItem('adminRefusedMessage', 'Connexion refusée : il y a déjà un administrateur connecté.');
    // Rediriger vers la page d'accueil
    window.location.href = '/';
    alert('Connexion refusée : il y a déjà un administrateur connecté.');
};

const adminDisconnected = () => displayMessage('Vous avez été déconnecté.');
console.log('Vous avez été déconnecté.');

socket.on(msg.WELCOME, init);
socket.on(msg.BYE, adminDisconnected);
