package org.d3javu.bd.models.post;

public record PostForReport(
        Long id,
        String title,
        String body,
        Integer likesCount,
        String createdAt,
        String authorFirstName,
        String authorLastName,
        String postLink
) {}


/*
id
title
body
---------
author id
author firstname lastname
link
---------
link for post

//likes count with link for likes
//comments count with link for comments
//images links
 */