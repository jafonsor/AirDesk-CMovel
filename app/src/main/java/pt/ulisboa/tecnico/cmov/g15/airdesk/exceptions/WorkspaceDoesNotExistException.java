package pt.ulisboa.tecnico.cmov.g15.airdesk.exceptions;

import pt.ulisboa.tecnico.cmov.g15.airdesk.AirDesk;
import pt.ulisboa.tecnico.cmov.g15.airdesk.domain.Workspace;

/**
 * Created by ist169408 on 01-05-2015.
 */
public class WorkspaceDoesNotExistException extends AirDeskException {
    public WorkspaceDoesNotExistException(String msg) { super(msg); }
}
