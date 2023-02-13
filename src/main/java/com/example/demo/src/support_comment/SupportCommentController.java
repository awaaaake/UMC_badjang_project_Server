package com.example.demo.src.support_comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.scholarship_comment.model.DeleteScholarshipCommentReq;
import com.example.demo.src.support_comment.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/supports/comment")
public class SupportCommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SupportCommentProvider supportCommentProvider;
    @Autowired
    private final SupportCommentService supportCommentService;
    private final JwtService jwtService;

    public SupportCommentController(SupportCommentProvider supportCommentProvider, SupportCommentService supportCommentService, JwtService jwtService){
        this.supportCommentProvider = supportCommentProvider;
        this.supportCommentService = supportCommentService;
        this.jwtService = jwtService;
    }

    /**
     * 지원금 댓글 조회 API
     * [GET] /supports/comment?support_idx
     * @return BaseResponse<List<GetSupportCommentRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetSupportCommentRes>> getSupportComment(@RequestParam(required = true) Integer support_idx) {
        try{
            List<GetSupportCommentRes> getSupportCommentRes = supportCommentProvider.getSupportComment(support_idx);
            return new BaseResponse<>(getSupportCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 댓글 작성 API
     * [POST] /supports/comment/new-comment
     */

    @ResponseBody
    @PostMapping("/new-comment")
    public BaseResponse<PostSupportCommentRes> createSupportComment(@RequestBody PostSupportCommentReq postSupportCommentReq) {
        if (postSupportCommentReq.getSupport_comment_content() == null) {
            return new BaseResponse<>(POST_COMMENT_EMPTY_CONTENT);
        }
        try {
            Integer userIdxByJwt = jwtService.getUserIdx();
            Integer user_idx = postSupportCommentReq.getUser_idx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_idx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostSupportCommentRes postSupportCommentRes = supportCommentService.createSupportComment(postSupportCommentReq);
            return new BaseResponse<>(postSupportCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 수정 API
     * [PATCH] /supports/comment/modify/:support_comment_idx
     */
    @ResponseBody
    @PatchMapping("/modify/{support_comment_idx}") // 게시글 작성자(userIdx)를 확인해서 맞으면 바꾸도록 할껀데 jwt토큰도 받아서 같이
    public BaseResponse<String> modifySupportComment(@PathVariable("support_comment_idx") Integer support_comment_idx, @RequestBody SupportComment supportComment) {
        try {
            Integer userIdxByJwt = jwtService.getUserIdx(); // 토큰은 헤더에 있음
            Integer user_idx = supportComment.getUser_idx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_idx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //수정할 댓글이 비었을 경우
            if(supportComment.getSupport_comment_content() == null){
                return new BaseResponse<>(POST_COMMENT_EMPTY_CONTENT);
            }

            PatchSupportCommentReq patchSupportCommentReq = new PatchSupportCommentReq(support_comment_idx, supportComment.getSupport_comment_content());
            supportCommentService.modifySupportComment(patchSupportCommentReq);

            String result = "댓글이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 삭제 API
     * [DELETE] /supports/comment/delete/:support_comment_idx
     */
    @ResponseBody
    @PatchMapping("/delete/{support_comment_idx}")
    public BaseResponse<String> deleteSupportComment(@PathVariable("support_comment_idx") Integer support_comment_idx, @RequestBody DeleteSupportCommentReq deleteSupportCommentReq) {
        try {

            Integer userIdxByJwt = jwtService.getUserIdx();
            Integer user_idx = deleteSupportCommentReq.getUser_idx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_idx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            Integer support_idx = deleteSupportCommentReq.getSupport_idx();
            Integer support_comment_idx_ByReqBody = deleteSupportCommentReq.getSupport_comment_idx();
            // PathVariable로 들어온 댓글 인덱스와 RequestBody로 받은 댓글 인덱스가 같은지 확인
            if(support_comment_idx != support_comment_idx_ByReqBody) {
                return new BaseResponse<>(PATCH_WRONG_COMMENT_INDEX);
            }

            supportCommentService.deleteSupportComment(support_idx, support_comment_idx);

            String result = "댓글이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}