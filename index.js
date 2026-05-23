const express = require('express');
const fs = require('fs');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// ── Users ──────────────────────────────────────────────
const USERS_FILE = 'users.json';

let users = [];
if (fs.existsSync(USERS_FILE)) {
    users = JSON.parse(fs.readFileSync(USERS_FILE));
}

function saveUsers() {
    fs.writeFileSync(USERS_FILE, JSON.stringify(users));
}

// ── Rooms ──────────────────────────────────────────────
const ROOMS_FILE = 'rooms.json';

let rooms = [];
if (fs.existsSync(ROOMS_FILE)) {
    rooms = JSON.parse(fs.readFileSync(ROOMS_FILE));
} else {
    rooms = [
        { name: "301", building: "GLE", time: "8:30 AM - 12:30 PM", schedule: "2023-10-30" },
        { name: "202", building: "RTL", time: "1:00 PM - 5:00 PM", schedule: "2023-10-31" }
    ];
    fs.writeFileSync(ROOMS_FILE, JSON.stringify(rooms));
}

function saveRooms() {
    fs.writeFileSync(ROOMS_FILE, JSON.stringify(rooms));
}

// ── Groups ─────────────────────────────────────────────
const GROUPS_FILE = 'groups.json';

let groups = [];
if (fs.existsSync(GROUPS_FILE)) {
    groups = JSON.parse(fs.readFileSync(GROUPS_FILE));
} else {
    groups = [];
    fs.writeFileSync(GROUPS_FILE, JSON.stringify(groups));
}

function saveGroups() {
    fs.writeFileSync(GROUPS_FILE, JSON.stringify(groups));
}

// ── Routes ─────────────────────────────────────────────
app.get('/', (req, res) => {
    res.send("Backend is running!");
});

// Register
app.post('/save-profile', (req, res) => {
    const { username, password } = req.body;
    if (!username || !password) {
        return res.status(400).send("Missing username or password");
    }

    const exists = users.find(u => u.username === username);
    if (exists) {
        return res.status(409).send("Username already taken");
    }

    users.push({ username, password });
    saveUsers();
    res.status(200).send("User registered successfully");
});

    users.push({ username, password });
    saveUsers();
    res.status(200).send("User registered successfully");
});

// Login
app.post('/login', (req, res) => {
    const { username, password } = req.body;
    const user = users.find(u => u.username === username && u.password === password);
    if (user) {
        res.status(200).send("Login successful");
    } else {
        res.status(401).send("Invalid username or password");
    }
});

// Get all rooms
app.get('/get-rooms', (req, res) => {
    res.json(rooms);
});

// Add a room
app.post('/save-room', (req, res) => {
    const { name, building, time, schedule } = req.body;
    if (!name || !building || !time || !schedule) {
        return res.status(400).send("Missing room data");
    }
    rooms.push({ name, building, time, schedule });
    saveRooms();
    res.status(200).send("Room added successfully");
});

// Delete a room
app.post('/delete-room', (req, res) => {
    const { name } = req.body;
    rooms = rooms.filter(r => r.name !== name);
    saveRooms();
    res.status(200).send("Room deleted successfully");
});

// ── Group Routes ───────────────────────────────────────
// Get all groups
app.get('/get-groups', (req, res) => {
    res.json(groups);
});

// Create a group
app.post('/create-group', (req, res) => {
    const { name, owner } = req.body;
    if (!name || !owner) {
        return res.status(400).send("Missing group name or owner");
    }
    if (groups.find(g => g.name === name)) {
        return res.status(409).send("Group name already taken");
    }

    const newGroup = {
        name: name,
        owner: owner,
        members: [owner]
    };
    groups.push(newGroup);
    saveGroups();
    res.status(200).json(newGroup);
});

    const exists = groups.find(g => g.name === name);
    if (exists) {
        return res.status(409).send("Group name already taken");
    }
    groups.push({ name, owner, members: [owner] });
    saveGroups();
    res.status(200).send("Group created successfully");
});

// Join a group
app.post('/join-group', (req, res) => {
    const { name, username } = req.body;
    const group = groups.find(g => g.name === name);
    if (!group) {
        return res.status(404).send("Group not found");
    }
    if (!group.members.includes(username)) {
        group.members.push(username);
        saveGroups();
    }
    res.status(200).send("Joined successfully");
});

app.post('/delete-group', (req, res) => {
    const { name, username } = req.body;
    const groupIndex = groups.findIndex(g => g.name === name);
    if (groupIndex === -1) {
        return res.status(404).send("Group not found");
    }
    if (groups[groupIndex].owner !== username) {
        return res.status(403).send("Only the owner can delete the group");
    }

    groups.splice(groupIndex, 1);
    if (group.members.includes(username)) {
        return res.status(400).send("Already a member");
    }
    group.members.push(username);
    saveGroups();
    res.status(200).send("Joined group successfully");
});

// Delete a group (owner only)
app.post('/delete-group', (req, res) => {
    const { name, username } = req.body;
    const group = groups.find(g => g.name === name);
    if (!group) {
        return res.status(404).send("Group not found");
    }
    if (group.owner !== username) {
        return res.status(403).send("Only the owner can delete this group");
    }
    groups = groups.filter(g => g.name !== name);
    saveGroups();
    res.status(200).send("Group deleted successfully");
});

app.get('/debug-users', (req, res) => {
    res.json(users);
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
