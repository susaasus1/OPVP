import React, {useEffect} from 'react';
import {BrowserRouter as Router, Route, Routes, useNavigate} from 'react-router-dom';
import {AppBar, Toolbar, Typography, Button, Container} from '@mui/material';
import Start from './Start';
import TableManager from './TableManager';

function App() {
    return (<Router>
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h5"
                            sx={{flexGrow: 1}}>
                    DB-client | Nurullaev & Sashina
                </Typography>
                <ResetButton/>
            </Toolbar>
        </AppBar>
        <Container>
            <ProtectedRoutes/>
        </Container>
    </Router>);
}

const ResetButton = () => {
    const navigate = useNavigate();

    const handleReset = () => {
        localStorage.removeItem('serverIP');
        localStorage.removeItem('serverPort');
        navigate('/');
    };

    return (<Button sx={{
        color: 'white',
        border: '2px solid black',
        borderRadius: '4px',
    }} onClick={handleReset}>
        Переключить сервер
    </Button>);
};

const ProtectedRoutes = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const storedIp = localStorage.getItem('serverIP');
        const storedPort = localStorage.getItem('serverPort');
        if (!storedIp || !storedPort) {
            navigate('/');
        }
    }, [navigate]);

    return (<Routes>
        <Route path="/main" element={<TableManager/>}/>
        <Route path="/" element={<Start/>}/>
    </Routes>);
};

export default App;
