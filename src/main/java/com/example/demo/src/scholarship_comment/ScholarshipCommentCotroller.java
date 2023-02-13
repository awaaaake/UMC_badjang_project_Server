package com.example.demo.src.scholarship_comment;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.scholarship_comment.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/scholarships/comment")
public class ScholarshipCommentCotroller {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ScholarshipCommentProvider scholarshipCommentProvider;
    @Autowired
    private final ScholarshipCommentService scholarshipCommentService;

    private final JwtService jwtService;

    public ScholarshipCommentCotroller(ScholarshipCommentProvider scholarshipCommentProvider, ScholarshipCommentService scholarshipCommentService, JwtService jwtService){
        this.scholarshipCommentProvider = scholarshipCommentProvider;
        this.scholarshipCommentService = scholarshipCommentService;
        this.jwtService = jwtService;
    }

    /**
     * 댓글 조회 API
     * [GET] /scholarship/comment?scholarship_idx=
     * @return BaseResponse<List<getScholarshipCommentRes>>
     */
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetScholarshipCommentRes>> getScholarshipComment(@RequestParam(required = true) Integer scholarship_idx) {
        try{
            // Get Users
            List<GetScholarshipCommentRes> getScholarshipCommentRes = scholarshipCommentProvider.getScholarshipComment(scholarship_idx);
            return new BaseResponse<>(getScholarshipCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 작성 API
     * [POST] /scholarship/comment/new-comment
     */
    @ResponseBody
    @PostMapping("/new-comment")
    public BaseResponse<PostScholarshipCommentRes> createScholarshipComment(@RequestBody PostScholarshipCommentReq postScholarshipCommentReq) {
        if (postScholarshipCommentReq.getScholarship_comment_content() == null) {
            return new BaseResponse<>(POST_COMMENT_EMPTY_CONTENT);
        }
        try {
            Integer userIdxByJwt = jwtService.getUserIdx();
            Integer user_idx = postScholarshipCommentReq.getUser_idx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_idx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostScholarshipCommentRes postScholarshipCommentRes = scholarshipCommentService.createScholarshipComment(postScholarshipCommentReq);
            return new BaseResponse<>(postScholarshipCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 수정 API
     * [PATCH] /scholarship/comment/modify/:scholarship_comment_idx
     */
    @ResponseBody
    @PatchMapping("/modify/{scholarship_comment_idx}") // 게시글 작성자(userIdx)를 확인해서 맞으면 바꾸도록 할껀데 jwt토큰도 받아서 같이
    public BaseResponse<String> modifyScholarshipComment(@PathVariable("scholarship_comment_idx") Integer scholarship_comment_idx, @RequestBody ScholarshipComment scholarshipComment) {
        try {
            Integer userIdxByJwt = jwtService.getUserIdx();
            Integer user_idx = scholarshipComment.getUser_idx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_idx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //수정할 댓글이 비었을 경우
            if(scholarshipComment.getScholarship_comment_content() == null){
                return new BaseResponse<>(POST_COMMENT_EMPTY_CONTENT);
            }

            PatchScholarshipCommentReq patchScholarshipCommentReq = new PatchScholarshipCommentReq(scholarship_comment_idx, scholarshipComment.getScholarship_comment_content());
            scholarshipCommentService.modifyScholarshipComment(patchScholarshipCommentReq);

            String result = "댓글이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 삭제 API
     * [DELETE] /scholarship/comment/delete/:scholarship_comment_idx
     */
    @ResponseBody
    @PatchMapping("/delete/{scholarship_comment_idx}")
    public BaseResponse<String> deleteScholarshipComment(@PathVariable("scholarship_comment_idx") Integer scholarship_comment_idx, @RequestBody DeleteScholarshipCommentReq deleteScholarshipCommentReq) {
        try {

            Integer userIdxByJwt = jwtService.getUserIdx();
            Integer user_idx = deleteScholarshipCommentReq.getUser_idx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_idx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            Integer scholarship_idx = deleteScholarshipCommentReq.getScholarship_idx();
            Integer scholarship_comment_idx_ByReqBody = deleteScholarshipCommentReq.getScholarship_comment_idx();
            // PathVariable로 들어온 댓글 인덱스와 RequestBody로 받은 댓글 인덱스가 같은지 확인
            if(scholarship_comment_idx != scholarship_comment_idx_ByReqBody) {
                return new BaseResponse<>(PATCH_WRONG_COMMENT_INDEX);
            }

            scholarshipCommentService.deleteScholarshipComment(scholarship_idx, scholarship_comment_idx);

            String result = "댓글이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
