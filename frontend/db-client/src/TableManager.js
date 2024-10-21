import React, { useState } from 'react';
import { TextField, Button, Container, Typography, Box, Alert, CircularProgress } from '@mui/material';
import { getTableValues, createTable, deleteTable } from './ApiService';

function TableManager() {
    const [tableName, setTableName] = useState('');
    const [tableData, setTableData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleGetTable = async () => {
        setLoading(true);
        setError('');
        setSuccessMessage('');
        try {
            const data = await getTableValues(tableName);
            setTableData(data);
            setSuccessMessage('Таблица успешно загружена.');
        } catch (error) {
            setError(`Ошибка: ${error?.status} - ${error?.statusText || 'Неизвестная ошибка'}`);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateTable = async () => {
        setLoading(true);
        setError('');
        setSuccessMessage('');
        try {
            await createTable(tableName);
            setSuccessMessage('Таблица успешно создана.');
        } catch (error) {
            setError(`Ошибка: ${error?.status} - ${error?.statusText || 'Неизвестная ошибка'}`);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteTable = async () => {
        setLoading(true);
        setError('');
        setSuccessMessage('');
        try {
            await deleteTable(tableName);
            setSuccessMessage('Таблица успешно удалена.');
        } catch (error) {
            setError(`Ошибка: ${error?.status} - ${error?.statusText || 'Неизвестная ошибка'}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container maxWidth="md">
            <Box sx={{ marginTop: 4 }}>
                <Typography variant="h4" gutterBottom>Управление таблицами</Typography>
                {error && <Alert severity="error">{error}</Alert>}
                {successMessage && <Alert severity="success">{successMessage}</Alert>}
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <TextField
                        label="Название таблицы"
                        value={tableName}
                        onChange={(e) => setTableName(e.target.value)}
                        fullWidth
                        variant="outlined"
                    />
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                        <Button variant="contained" color="primary" onClick={handleGetTable}>
                            Загрузить таблицу
                        </Button>
                        <Button variant="contained" color="success" onClick={handleCreateTable}>
                            Создать таблицу
                        </Button>
                        <Button variant="contained" color="error" onClick={handleDeleteTable}>
                            Удалить таблицу
                        </Button>
                    </Box>
                    {loading && <CircularProgress />}
                    {tableData && (
                        <Box>
                            <Typography variant="h6">Содержимое таблицы:</Typography>
                            <pre>{JSON.stringify(tableData, null, 2)}</pre>
                        </Box>
                    )}
                </Box>
            </Box>
        </Container>
    );
}

export default TableManager;
