const express = require('express');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// In-memory storage for rooms (this will reset if the server restarts)
let rooms = [
    { name: "301", building: "GLE", time: "8:30 AM - 12:30 PM", schedule: "Monday" },
    { name: "202", building: "RTL", time: "1:00 PM - 5:00 PM", schedule: "Tuesday" }
];

app.post('/save-profile', (req, res) => {
    const { username, password } = req.body;
    console.log(`Received registration for: ${username}`);

    if (username && password) {
        // Success response
        res.status(200).send("User registered successfully");
    } else {
        res.status(400).send("Missing username or password");
    }
});

// GET all rooms
app.get('/get-rooms', (req, res) => {
    res.json(rooms);
});

// POST to add a new room
app.post('/save-room', (req, res) => {
    const { name, building, time, schedule } = req.body;
    if (name && building && time && schedule) {
        rooms.push({ name, building, time, schedule });
        res.status(200).send("Room added successfully");
    } else {
        res.status(400).send("Missing room data");
    }
});

// POST to delete a room
app.post('/delete-room', (req, res) => {
    const { name } = req.body;
    rooms = rooms.filter(r => r.name !== name);
    res.status(200).send("Room deleted successfully");
});

app.get('/', (req, res) => {
    res.send("Backend is running!");
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
