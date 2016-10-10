package com.wfcrc.repository;

import com.wfcrc.pojos.Document;
import com.wfcrc.pojos.Program;

import java.util.List;

/**
 * Created by maria on 10/6/16.
 */
public interface Repository<T> {

    List<T> getAll();

}
