package dev.zontreck.otemod.items.tags;

import java.util.UUID;

public enum ModIDs {
    NULL(0,0),
    ZNI(0x9f, 0x9f),
    OTE(5292022,1182023), // The date range of mod creation, then the day when this feature was added
    ITEM_STATS(154301182023L, 0x9f);

    private UUID ID;

    ModIDs(long A, long B)
    {
        ID=new UUID(A,B);
    }

    public UUID get()
    {
        return ID;
    }
}
