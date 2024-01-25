package it.uniroma2.dicii.ispw.model.announcement.dao;

import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.announcement.Announcement;

import java.util.List;

public interface AnnouncementDAO {

    public String insertAnnouncement(Announcement announcement) throws ItemAlreadyExistsException;
    public List<Announcement> getAllAnnouncement();
}
