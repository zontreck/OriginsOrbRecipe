package dev.zontreck.otemod.commands.teleport;

import java.time.Instant;
import java.util.UUID;

public class TeleportContainer implements Comparable{
    public UUID FromPlayer;
    public UUID ToPlayer;
    public long StartedAt;
    
    public boolean has_expired(){
        if(Instant.now().getEpochSecond() > (StartedAt + (60)))
        {
            return true;
        }else return false;
    }

    public TeleportContainer (UUID From, UUID To)
    {
        FromPlayer = From;
        ToPlayer=To;

        StartedAt = Instant.now().getEpochSecond();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof TeleportContainer){
            TeleportContainer cont = (TeleportContainer)o;
            if(cont.FromPlayer != FromPlayer){
                return -1;
            }else {
                if(cont.ToPlayer != ToPlayer)return 1;
                else return 0;
            }
        }else return -1;
    }
}
