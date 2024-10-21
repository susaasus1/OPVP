import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TableManager from './TableManager';
import Start from './Start';
import { Container, AppBar, Toolbar, Typography } from '@mui/material';

function App() {
    return (
        <Router>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6">DB-client</Typography>
                </Toolbar>
            </AppBar>
            <Container>
                <Routes>
                    <Route path="/main" element={<TableManager />} />
                    <Route path="/" element={<Start />} />
                </Routes>
            </Container>
        </Router>
    );
}

export default App;
