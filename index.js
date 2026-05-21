const express = require('express');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

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

app.get('/', (req, res) => {
    res.send("Backend is running!");
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
