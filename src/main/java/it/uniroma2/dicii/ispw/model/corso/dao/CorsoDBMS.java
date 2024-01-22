package it.uniroma2.dicii.ispw.model.corso.dao;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CorsoDBMS implements CorsoDAO {

    @Override
    public Corso getCorsoByNome(String nome) throws ItemNotFoundException, DbConnectionException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Corso corso = new Corso();
        try {
            conn = DbConnection.getConnection();
            String sql = "select * from corso where nome=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, nome);

            resultSet = statement.executeQuery();

            if(!resultSet.next()) throw new ItemNotFoundException("Nessun corso con nome: " + nome);

            corso.setName(resultSet.getString("nome"));
            corso.setStartDate(resultSet.getDate("data_inizio"));

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore di connessione al db", e);
            return corso;
        }
        return corso;
    }

    @Override
    public List<Corso> getAllCorsi() throws Exception {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Corso> corsoList = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "select * from corso";

            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(!resultSet.next()) throw new ItemNotFoundException("Nessun corso presente");

            do {
                Corso c = new Corso(resultSet.getString("nome"), resultSet.getDate("data_inizio"));
                corsoList.add(c);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore di connessione al db", e);
            return corsoList;
        }
        return corsoList;
    }

    @Override
    public String addCorso(Corso corso) throws Exception {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String res = "Errore nell'inserimento";
        try {
            conn = DbConnection.getConnection();
            String sql = "insert into corso(nome, data_inizio) values(?, ?)";

            statement = conn.prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setDate(2, new java.sql.Date(corso.getStartDate().getTime()));
            statement.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore di connessione al db", e);
            return res;
        }
        return "Corso aggiunto correttamente";
    }

    @Override
    public String removeCorso(Corso corso) throws Exception {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String res = "Errore nella rimozione";
        try {
            conn = DbConnection.getConnection();
            String sql = "delete from corso where nome=? and data_inizio=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setDate(2, new java.sql.Date(corso.getStartDate().getTime()));
            statement.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore di connessione al db", e);
            return res;
        }
        return "Corso rimosso correttamente";
    }
}
