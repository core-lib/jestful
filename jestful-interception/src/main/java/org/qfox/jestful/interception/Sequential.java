package org.qfox.jestful.interception;

import java.util.Comparator;

/**
 * Created by yangchangpei on 17/8/23.
 */
public interface Sequential {

    Comparator<Sequential> COMPARATOR = new Comparator<Sequential>() {
        @Override
        public int compare(Sequential a, Sequential b) {
            return a.getSequence() > b.getSequence() ? 1 : a.getSequence() < b.getSequence() ? -1 : 0;
        }
    };

    int getSequence();

}
