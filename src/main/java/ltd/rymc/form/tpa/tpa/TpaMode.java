package ltd.rymc.form.tpa.tpa;

import ltd.rymc.form.tpa.TpaForm;

public enum TpaMode {
    TPA,
    TPAHERE;

    public static String getDescription(TpaMode mode){
        return mode == TPA ? TpaForm.lang().tpa() : TpaForm.lang().tpaHere();
    }

    public static String getAcceptCommand(TpaMode mode){
        return mode == TPA ? TpaForm.config().tpaAcceptCommand() : TpaForm.config().tpaHereAcceptCommand();
    }

    public static String getDenyCommand(TpaMode mode){
        return mode == TPA ? TpaForm.config().tpaDenyCommand() : TpaForm.config().tpaHereDenyCommand();
    }
}
