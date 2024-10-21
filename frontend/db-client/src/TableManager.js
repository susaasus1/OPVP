import React, { useState, useEffect } from 'react';
import {
    TextField,
    Button,
    Container,
    Typography,
    Box,
    Alert,
    CircularProgress,
    List,
    ListItem,
    ListItemText
} from '@mui/material';
import { getTableValues, createTable, deleteTable, getAllTables } from './ApiService';

function TableManager() {
    const [tableName, setTableName] = useState('');
    const [tableData, setTableData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [tables, setTables] = useState([]);
    const [loadingTables, setLoadingTables] = useState(true);

    useEffect(() => {
        const fetchTables = async () => {
            try {
                const data = await getAllTables();
                setTables(data);
                setLoadingTables(false);
            } catch (error) {
                setError('Ошибка при загрузке таблиц');
                setLoadingTables(false);
            }
        };

        fetchTables();
    }, []);

    const handleGetTable = async (table) => {
        setLoading(true);
        setError('');
        setSuccessMessage('');
        setTableName(table);

        try {
            const data = await getTableValues(table);
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
            const data = await getAllTables();
            setTables(data);
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
            // Обновление списка таблиц после удаления
            const data = await getAllTables();
            setTables(data);
        } catch (error) {
            setError(`Ошибка: ${error?.status} - ${error?.statusText || 'Неизвестная ошибка'}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container maxWidth="lg">
            <Box sx={{ marginTop: 4, display: 'flex', height: '80vh', gap: 2 }}>
                <Box sx={{ flex: 1, borderRight: '1px solid #ccc', padding: 2 }}>
                    <Typography variant="h4" gutterBottom>Управление таблицами</Typography>
                    {error && <Alert severity="error">{error}</Alert>}
                    {successMessage && <Alert severity="success">{successMessage}</Alert>}
                    {loadingTables ? (
                        <CircularProgress />
                    ) : (
                        <>
                            <List>
                                {tables.map((table) => (
                                    <ListItem button key={table} onClick={() => handleGetTable(table)}>
                                        <ListItemText primary={table} />
                                    </ListItem>
                                ))}
                            </List>
                            <TextField
                                label="Название таблицы"
                                value={tableName}
                                onChange={(e) => setTableName(e.target.value)}
                                fullWidth
                                variant="outlined"
                                sx={{ marginTop: 2 }}
                            />
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', marginTop: 1 }}>
                                <Button variant="contained" color="success" onClick={handleCreateTable}>
                                    Создать таблицу
                                </Button>
                                <Button variant="contained" color="error" onClick={handleDeleteTable}>
                                    Удалить таблицу
                                </Button>
                            </Box>
                        </>
                    )}
                </Box>

                <Box sx={{ flex: 1, padding: 2 }}>
                    <Typography variant="h4" gutterBottom>Содержимое таблицы</Typography>
                    {loading ? (
                        <CircularProgress />
                    ) : tableData ? (
                        <Box>
                            <Typography variant="h6">Содержимое таблицы:</Typography>
                            <pre>{JSON.stringify(tableData, null, 2)}</pre>
                        </Box>
                    ) : (
                        <Typography variant="body1">Пожалуйста, выберите таблицу, чтобы увидеть ее содержимое.</Typography>
                    )}
                </Box>
            </Box>
        </Container>
    );
}

export default TableManager;
