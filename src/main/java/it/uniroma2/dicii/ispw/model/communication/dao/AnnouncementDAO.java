package it.uniroma2.dicii.ispw.model.communication.dao;

import it.uniroma2.dicii.ispw.model.communication.Announcement;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;

import java.util.List;

public interface AnnouncementDAO {
    public String insertAnnouncement(Announcement announcement) throws ItemAlreadyExistsException;
    public List<Announcement> getAllAnnouncement();
}
