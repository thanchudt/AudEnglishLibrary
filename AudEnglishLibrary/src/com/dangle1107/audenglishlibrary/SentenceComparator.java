package com.dangle1107.audenglishlibrary;

import java.util.Comparator;

public class SentenceComparator implements Comparator<Sentence>
{
    public int compare(Sentence left, Sentence right) {
    	if (left.getPosition() < right.getPosition())
        	return -1;
        if (left.getPosition() > right.getPosition())
    		return 1;
        return 0;
    }
}
