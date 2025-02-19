import * as msg from './messageConstants.js';
import { displayMessage } from './tools.js';

const socket = io();
let currentSubject = '';

const init = () => {
    console.log('Connected as voter');
    socket.emit(msg.VOTANT);
    initMessageListener();
    initVoteButtons();
}

const initMessageListener = () => {
    socket.on(msg.START_VOTE, voteStarts);
    socket.on(msg.END_VOTE, (results) => {
        voteEnds(results);
    });
    socket.on(msg.VOTE_UPDATED, (voterId, vote) => {
        if (voterId === socket.id) {
            document.getElementById('voteActuelUtilisateur').textContent = `Votre vote : ${vote.toUpperCase()}`;
            enableVoteButtons(true);
        }
    });
    socket.on(msg.ADMIN_DISCONNECTED, adminDisconnected);
    socket.on(msg.BYE, () => {
        console.log('Disconnected from server');
        document.getElementById('message').textContent = 'Déconnecté du serveur';
    });
}

const initVoteButtons = () => {
    const buttons = document.querySelectorAll("button");
    buttons.forEach(button => {
        button.addEventListener('click', () => {
            const vote = button.id;
            console.log(`Voting: ${vote}`);
            socket.emit(msg.NEW_VOTE, vote);
            
            document.getElementById('voteActuelUtilisateur').textContent = `Votre vote actuel : ${vote.toUpperCase()}`;
            document.getElementById('message').textContent = 'Vote enregistré. Vous pouvez changer votre vote tant que le scrutin est ouvert.';
            
            enableVoteButtons(true);
        });
    });
}

const voteStarts = (subject) => {
    currentSubject = subject;
    document.getElementById('EtatVote').textContent = 'Ouvert';
    document.getElementById('voteActuel').textContent = subject;
    document.getElementById('voteActuelUtilisateur').textContent = '';
    enableVoteButtons(true);
    
    const resultsDiv = document.getElementById('resultats');
    if (resultsDiv) {
        resultsDiv.innerHTML = '';
    }
}

const voteEnds = (results) => {
    document.getElementById('EtatVote').textContent = 'Fermé';
    enableVoteButtons(false);
    document.getElementById('voteActuel').textContent = 'Vote terminé';
    
    const voteCounts = {
        pour: 0,
        contre: 0,
        nppv: 0,
        abstention: 0
    };

    results.forEach(([_, vote]) => {
        voteCounts[vote]++;
    });

    const resultsDiv = document.getElementById('resultats');
    resultsDiv.innerHTML = `
        <div class="mt-4">
            <h3 class="text-xl font-semibold mb-2">Résultats du vote: "${currentSubject}"</h3>
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
}

const adminDisconnected = () => {
    document.getElementById('EtatVote').textContent = 'Admin déconnecté';
    document.getElementById('voteActuel').textContent = '';
    document.getElementById('voteActuelUtilisateur').textContent = '';
    enableVoteButtons(false);
}

const enableVoteButtons = (enable) => {
    const buttons = document.querySelectorAll("button");
    buttons.forEach(button => button.disabled = !enable);
}

socket.on(msg.WELCOME, init);