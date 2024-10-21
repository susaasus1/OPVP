import React, { useState, useEffect  } from 'react';
import { TextField, Button, Container, Typography, Box, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';

function Start() {
    const [ip, setIp] = useState('');
    const [port, setPort] = useState('');
    const [saved, setSaved] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const storedIp = localStorage.getItem('serverIP');
        const storedPort = localStorage.getItem('serverPort');
        if (storedIp && storedPort) {
            navigate('/main');
        }
    }, [navigate]);

    const handleSave = () => {
        if (validateIp(ip) && validatePort(port)) {
            localStorage.setItem('serverIP', ip);
            localStorage.setItem('serverPort', port);
            setSaved(true);
            setError('');

            setTimeout(() => {
                navigate('/main');
            }, 1000);
        } else {
            setError('Введите корректные данные IP и порта.');
        }
    };

    const validateIp = (ip) => {
        const ipRegex = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^localhost$/;
        return ipRegex.test(ip);
    };

    const validatePort = (port) => {
        const portNumber = parseInt(port, 10);
        return portNumber > 0 && portNumber < 65536;
    };

    return (
        <Container maxWidth="sm">
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'center',
                    alignItems: 'center',
                    minHeight: '80vh',
                    backgroundColor: '#f4f7f6',
                    padding: 3,
                    borderRadius: 2,
                    boxShadow: 3
                }}>
                {saved ? (
                    <Typography variant="h5" gutterBottom>
                        Данные сохранены: IP: {ip}, Порт: {port}
                    </Typography>
                ) : (
                    <Box sx={{ width: '100%' }}>
                        <Typography variant="h4" align="center" gutterBottom>
                            Введите IP и Порт сервера
                        </Typography>
                        {error && <Alert severity="error">{error}</Alert>}
                        <TextField
                            fullWidth
                            margin="normal"
                            label="IP"
                            variant="outlined"
                            value={ip}
                            onChange={(e) => setIp(e.target.value)}
                        />
                        <TextField
                            fullWidth
                            margin="normal"
                            label="Порт"
                            variant="outlined"
                            value={port}
                            onChange={(e) => setPort(e.target.value)}
                        />
                        <Button
                            fullWidth
                            variant="contained"
                            color="primary"
                            onClick={handleSave}
                            sx={{ marginTop: 2 }}
                        >
                            Сохранить
                        </Button>
                    </Box>
                )}
            </Box>
        </Container>
    );
}

export default Start;
