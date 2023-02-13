package com.example.demo.src.bookmark;


import com.example.demo.config.BaseException;
import com.example.demo.src.bookmark.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
public class BookmarkService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookmarkDao bookmarkDao;

    private final BookmarkProvider bookmarkProvider;

    @Autowired
    public BookmarkService(BookmarkProvider bookmarkProvider, BookmarkDao bookmarkDao) {
        this.bookmarkProvider = bookmarkProvider;
        this.bookmarkDao = bookmarkDao;

    }

    public String postBookmarkScholarship(PostBookmarkScholarshipReq postBookmarkScholarshipReq) throws BaseException {
        try {
            String postBookmarkScholarshipRes = bookmarkDao.postBookmarkScholarship(postBookmarkScholarshipReq);
            return postBookmarkScholarshipRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String postBookmarkSupport(PostBookmarkSupportReq postBookmarkSupportReq) throws BaseException {
        try {
            String postBookmarkSupportRes = bookmarkDao.postBookmarkSupport(postBookmarkSupportReq);
            return postBookmarkSupportRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String postBookmarkBoard(PostBookmarkBoardReq postBookmarkBoardReq) throws BaseException {
        try {
            String postBookmarkBoardRes = bookmarkDao.postBookmarkBoard(postBookmarkBoardReq);
            return postBookmarkBoardRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String postBookmarkSchool(PostBookmarkSchoolReq postBookmarkSchoolReq) throws BaseException {
        try {
            String postBookmarkSchoolRes = bookmarkDao.postBookmarkSchool(postBookmarkSchoolReq);
            return postBookmarkSchoolRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String postBookmarkTotal(PostBookmarkTotalReq postBookmarkTotalReq) throws BaseException {
        try {
            String postBookmarkTotalRes = bookmarkDao.postBookmarkTotal(postBookmarkTotalReq);
            return postBookmarkTotalRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}



