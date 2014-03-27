package com.jesm3.newDualis.stupla;

import java.util.Comparator;
import java.util.GregorianCalendar;

public class VorlesungComparator implements Comparator<Vorlesung>
{
    public int compare(Vorlesung t1, Vorlesung t2)
    {
        GregorianCalendar val1 = new GregorianCalendar();
        val1.setTime(t1.getUhrzeitVon());
        GregorianCalendar val2 = new GregorianCalendar();
        val2.setTime(t2.getUhrzeitVon());
        return (val1 == val2 ? 0 : val1.compareTo(val2));
    }
}