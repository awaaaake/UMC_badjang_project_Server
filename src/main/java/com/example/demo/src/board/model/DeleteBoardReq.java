package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class DeleteBoardReq {
    private int post_idx;
    private int user_idx;

    public DeleteBoardReq(){}
}


