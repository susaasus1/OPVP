import React, {useState, useEffect} from 'react';
import {
    TextField,
    Button,
    Container,
    Typography,
    Box,
    Alert,
    CircularProgress,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper
} from '@mui/material';
import {getTableValues, createTable, deleteTable, getAllTables} from './ApiService';

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
            <Box sx={{marginTop: 4, display: 'flex', height: '80vh', gap: 2}}>

                <Box sx={{flex: '0 0 25%', borderRight: '2px solid black', padding: 2}}>
                    <Typography variant="h5" gutterBottom>Список таблиц</Typography>
                    {error && <Alert severity="error">{error}</Alert>}
                    {successMessage && <Alert severity="success">{successMessage}</Alert>}
                    {loadingTables ? (
                        <CircularProgress/>
                    ) : (
                        <TableContainer component={Paper}>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Название таблицы</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {tables.map((table) => (
                                        <TableRow
                                            key={table}
                                            onClick={() => handleGetTable(table)}
                                            sx={{cursor: 'pointer'}}
                                        >
                                            <TableCell>{table}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    )}
                </Box>

                <Box sx={{flex: '0 0 40%', borderRight: '2px solid black', padding: 2}}>
                    <Typography variant="h5" gutterBottom>Управление таблицами</Typography>
                    <TextField
                        label="Название таблицы"
                        value={tableName}
                        onChange={(e) => setTableName(e.target.value)}
                        fullWidth
                        variant="outlined"
                        sx={{marginTop: 2}}
                    />
                    <Box sx={{display: 'flex', justifyContent: 'space-between', marginTop: 2}}>
                        <Button variant="contained" color="success" onClick={handleCreateTable} fullWidth
                                sx={{marginRight: 1}}>
                            Создать таблицу
                        </Button>
                        <Button variant="contained" color="error" onClick={handleDeleteTable} fullWidth
                                sx={{marginLeft: 1}}>
                            Удалить таблицу
                        </Button>
                    </Box>
                </Box>

                <Box sx={{flex: '0 0 35%', padding: 2}}>
                    <Typography variant="h5" gutterBottom>Содержимое таблицы</Typography>
                    {loading ? (
                        <CircularProgress/>
                    ) : tableData ? (
                        <TableContainer component={Paper}>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        {Object.keys(tableData[0]).map((key) => (
                                            <TableCell key={key}>{key}</TableCell>
                                        ))}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {tableData.map((row, index) => (
                                        <TableRow key={index}>
                                            {Object.values(row).map((value, idx) => (
                                                <TableCell key={idx}>{value}</TableCell>
                                            ))}
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    ) : (
                        <Typography>Выберите таблицу, чтобы просмотреть данные.</Typography>
                    )}
                </Box>
            </Box>
        </Container>
    );
}

export default TableManager;
