import * as msg from '../utils/messageConstants.js';

const VOTE_ROOM = 'voteRoom';

export default class IOController {
    #io;
    #admin;
    #votants;
    #votes;
    #currentVoteSubject;

    constructor(io) {
        this.#io = io;
        this.#votants = new Set();
        this.#votes = new Map();
        this.#currentVoteSubject = null;
    }

    registerSocket(socket) {
        console.log(`new connection with id ${socket.id}`);
        socket.emit(msg.WELCOME);
        socket.on(msg.ADMIN, () => this.adminConnecting(socket));
        socket.on(msg.VOTANT, () => this.votantConnecting(socket));
        
        socket.on('disconnect', () => {
            if (socket === this.#admin) {
                this.adminIsDisconnecting();
            } else if (this.#votants.has(socket)) {
                this.votantDisconnecting(socket);
            }
        });
    }

    votantDisconnecting(socket) {
        this.#votants.delete(socket);
        this.#votes.delete(socket.id);
        if (this.#admin) {
            this.#admin.emit('voter_disconnect');
        }
    }

    adminConnecting(socket) {
        if (this.#admin) {
            socket.emit(msg.ADMIN_REFUSED);
        } else {
            this.#admin = socket;
            socket.emit(msg.ADMIN_ACCEPTED);
            for (let i = 0; i < this.#votants.size; i++) {
                socket.emit(msg.VOTANT);
            }
            socket.on('disconnect', () => this.adminIsDisconnecting());
            socket.on(msg.START_VOTE, (subject) => this.startVote(subject));
            socket.on(msg.END_VOTE, () => this.endVote());
        }
    }

    adminIsDisconnecting() {
        if (this.#admin) {
            this.#admin.emit(msg.BYE);
            this.#io.emit(msg.ADMIN_DISCONNECTED);
        }
        this.#admin = undefined;
    }

    startVote(subject) {
        this.#votes.clear();
        this.#currentVoteSubject = subject;
        this.#io.emit(msg.START_VOTE, subject);
    }

    endVote() { 
        const results = Array.from(this.#votes.entries());
        this.#currentVoteSubject = null;
        this.#votes.clear();
        this.#io.emit(msg.END_VOTE, results);
    }

    votantConnecting(socket) {
        this.#votants.add(socket);
        socket.on(msg.NEW_VOTE, vote => this.voteReceived(socket, vote));
        
        if (this.#currentVoteSubject) {
            socket.emit(msg.START_VOTE, this.#currentVoteSubject);
        }
        
        if (this.#admin) {
            this.#admin.emit(msg.VOTANT);
        }
    }

    voteReceived(votant, vote) {
        this.#votes.set(votant.id, vote);
        this.#io.emit(msg.VOTE_UPDATED, votant.id, vote);
        if (this.#admin) {
            this.#admin.emit(msg.NEW_VOTE, votant.id, vote);
        }
    }
}
