package com.movie.cast.service.impl;

import com.cast.domain.entity.Cast;
import com.movie.cast.mapper.CastMapper;
import com.movie.cast.service.ICastService;
import com.movie.utils.DataGridView;
import com.movie.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ICastServiceImpl implements ICastService {

    @Autowired
    private CastMapper castMapper;

    @Override
    public DataGridView getMovieCast(Integer  movieId) {
        try {
            List<Cast> movieCast = castMapper.getMovieCast(movieId);
            if (movieCast.size() <1 ) return Utils.resSuccess(200, "", null);
            HashMap<String, Object> castMap = new HashMap<>();
            // 导演
            ArrayList<Cast> cast1 = new ArrayList<>();
            // 演员
            ArrayList<Cast> cast2 = new ArrayList<>();
            for (Cast cast : movieCast) {
                if (cast.getCastType() == 143) {
                    cast1.add(cast);
                    if(cast.getRole() != null) cast2.add(cast);
                }
                else cast2.add(cast);
            }
            castMap.put("cast1", cast1);
            castMap.put("cast2", cast2);
            return Utils.resSuccess(200, "",  castMap);
        } catch (Exception e) {
            return Utils.resFailure(400, "");
        }
    }
}
