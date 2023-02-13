package com.example.demo.src.menu;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.menu.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@RestController
@RequestMapping("")
public class MenuController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MenuProvider menuProvider;
    @Autowired
    private  final JwtService jwtService;




    public MenuController(MenuProvider menuProvider, JwtService jwtService){
        this.menuProvider = menuProvider;
        this.jwtService = jwtService;
    }

    /**
     * 전국소식 조회 API
     * [GET] /menu/total
     * @return BaseResponse<List<GetTotalRes>>
     */
    //Query String
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @GetMapping("/menu/total/{user_idx}") // (GET) 127.0.0.1:9000/menu/total
    public BaseResponse<GetListRes> getTotal(@PathVariable("user_idx") int user_idx) {
        try{
                GetListRes getScholarshipRes = menuProvider.getTotal(user_idx);
                return new BaseResponse<>(getScholarshipRes);

        } catch(BaseException exception){
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 인기글 조회 API
     * [GET] /menu/popular
     * @return BaseResponse<List<GetPopularRes>>
     */
    //Query String
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @GetMapping("/menu/popular") // (GET) 127.0.0.1:9000/menu/popular
    public BaseResponse<List<GetPopularRes>> getPopular() {
        try{

            List<GetPopularRes> getPopularRes = menuProvider.getPopular();
            return new BaseResponse<>(getPopularRes);

        } catch(BaseException exception){
            System.out.println(exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //우리학교 장학금 조회
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @GetMapping("/menu/school/{user_idx}")
    public BaseResponse<List<GetSchoolRes>> getSchool(@PathVariable("user_idx")int user_idx) {
            try {
                int idx = jwtService.getUserIdx();
                if(idx != user_idx){
                    return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
                }

                else if(user_idx == 0){
                    return new BaseResponse<>(BaseResponseStatus.USERS_EMPTY_USER_IDX);
                }

                List<GetSchoolRes> getSchoolRes = menuProvider.getSchool(user_idx);
                return new BaseResponse<>(getSchoolRes);

            } catch (BaseException exception) {
                System.out.println(exception);
                return new BaseResponse<>((exception.getStatus()));
            }

    }
}
