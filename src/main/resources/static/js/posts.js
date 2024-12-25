$(document).ready(function (){
    $('.comments-button').click(function (event){
        event.preventDefault();
        console.log(event);
        let href = event.currentTarget.href;
        let commentsWrapper = event.currentTarget.parentNode.parentNode.parentNode.parentNode.nextElementSibling;
        // let forRemove = event.currentTarget.parentNode.parentNode.parentNode.parentNode.nextElementSibling;
        // let resp = getComments(event);
        getComments(href, commentsWrapper);
    });

    $('.post_like-form').submit(function (event){
        event.preventDefault();
        likePost(event);
    });
    $('.post_unlike-form').submit(function (event){
        event.preventDefault();
        likePost(event);
    })

});

// $(document).on(function (){
//     $('.like-form').off('submit');
//     $('.like-form').on('submit',function (event){
//         event.preventDefault();
//         console.log("123");
//         like(event.currentTarget.href);
//     });
// });


function getComments(href, commentsWrapper){

    commentsWrapper = commentsWrapper || undefined;

    // console.log(href)

    // let href = event.currentTarget.href

    // event.nextElementSibling

    // console.log(event.currentTarget.parentNode.parentNode.parentNode.parentNode.nextElementSibling.className);

    if(commentsWrapper !== undefined && commentsWrapper !== null){
        commentsWrapper.parentNode.removeChild(commentsWrapper);
        return;
    }

    // console.log(event.currentTarget.parentNode.parentNode.parentNode.nextSibling)

    // if(document.getElementsByClassName('comments-wrapper').length > 0){
    //     return;
    // }

    let response = $.get({
        url: href,
        dataType: 'json',
    });
    let t = response.done(function (data, textStatus, jqXHR){
        let splittedPath = href.split("/")
        let id = 'post' + splittedPath[splittedPath.length-1]

        let item = document.createElement('div');
        item.innerText = jqXHR.responseText;

        let resp = JSON.parse(jqXHR.responseText);
        let currentUser = resp['currentUser'];

        let comments = resp['comments'];

        let commentsWrapper = document.createElement('div');
        commentsWrapper.className = 'comments-wrapper';

        let h = document.createElement('h3');
        h.textContent = 'Comments';

        commentsWrapper.append(h);

        let createCommentWrapper = document.createElement('div');
        createCommentWrapper.className = 'create-comment_wrapper';

        let commentCreateForm = document.createElement('form');
        // commentCreateForm.action = '/api/v1/comments/create';
        commentCreateForm.className = 'comment-create-form'
        commentCreateForm.action = '/posts/'+splittedPath[splittedPath.length-1]+'/comment/new';
        commentCreateForm.method = 'post';

        let commentCreateButton = document.createElement('button');
        commentCreateButton.type = 'submit';
        commentCreateButton.innerText = 'create comment';
        commentCreateButton.className = 'comment-create-button';

        let commentTextArea = document.createElement('textarea');
        commentTextArea.className = 'comment-create-body';
        commentTextArea.ariaMultiLine = 'true';
        commentTextArea.placeholder = 'write your opinion';
        commentTextArea.spellcheck = false;
        commentTextArea.ariaRequired = 'true';
        commentTextArea.ariaValueMin = '30';

        let textAreaLabel = document.createElement('label');
        textAreaLabel.htmlFor = 'comment-create-body';
        textAreaLabel.append(commentTextArea);

        let buttonLabel = document.createElement('label');
        buttonLabel.className = 'create-comment-button_wrapper';
        buttonLabel.htmlFor = 'comment-create-button';
        buttonLabel.append(commentCreateButton);

        commentCreateForm.append(textAreaLabel);
        commentCreateForm.append(buttonLabel);
        commentCreateForm.addEventListener('submit', createComment);

        createCommentWrapper.append(commentCreateForm);

        commentsWrapper.append(createCommentWrapper);

        comments.forEach(el =>{
            let authorId = el['author']['id'];
            let img = document.createElement('img');
            img.src = '/api/v1/users/'+authorId+'/avatar';
            img.alt = 'avatar';
            img.className = 'author-avatar_post'; //change

            let p = document.createElement('p');
            p.innerText = el['author']['firstName'] + " " + el['author']['lastName'];
            p.className = 'author';

            let authorInfo = document.createElement('a');
            authorInfo.href = '/users/'+ authorId;
            authorInfo.className = 'author-info';
            authorInfo.append(img, p);

            let authorWrapper = document.createElement('div');
            authorWrapper.className = 'author-wrapper';
            authorWrapper.append(authorInfo);

            let commentBody = document.createElement('div');
            commentBody.className = 'comment-body';
            let commentBodyP = document.createElement('p');
            commentBodyP.innerText = el['body']
            commentBody.append(commentBodyP);

            let commentActions = document.createElement('div');
            let likeBlock = document.createElement('div');
            let likeForm = document.createElement('form');
            let unlikeForm = document.createElement('form');
            let likeButton = document.createElement('button');
            let unlikeButton = document.createElement('button');
            let likeCount = document.createElement('p');
            let editForm = document.createElement('form');
            let editButton = document.createElement('button');

            likeCount.innerText = el['likes'].length;

            likeButton.className = 'comment-like-button';
            likeButton.type = 'submit';
            likeButton.innerText = 'like';

            unlikeButton.className = 'comment-like-button';
            unlikeButton.type = 'submit';
            unlikeButton.innerText = 'unlike';

            likeForm.append(likeCount);
            likeForm.action = '/posts/'+el['postId']+'/comment/'+el['id']+'/like'; //change
            likeForm.method = 'post';
            likeForm.className = 'like-form';

            unlikeForm.append(likeCount);
            unlikeForm.action = '/posts/'+el['postId']+'/comment/'+el['id']+'/unlike'; //change
            unlikeForm.method = 'post';
            unlikeForm.className = 'unlike-form';

            let isLiked = false;

            for(let x of el['likes']){
                // console.log(x);
                if (x === currentUser['id']){
                    isLiked = true;
                    break;
                }
            }

            let unlikeButtonWrapper = document.createElement('div');
            unlikeButtonWrapper.className = 'unlike-button_wrapper';
            unlikeButtonWrapper.append(unlikeButton)

            let likeButtonWrapper = document.createElement('div');
            likeButtonWrapper.className = 'like-button_wrapper';
            likeButtonWrapper.append(likeButton);

            if(isLiked){
                unlikeForm.append(unlikeButtonWrapper);
                unlikeForm.addEventListener('submit', like);
                likeBlock.append(unlikeForm);
            }else{
                likeForm.append(likeButtonWrapper);
                likeForm.addEventListener('submit', like);
                likeBlock.append(likeForm);
            }

            likeBlock.append(likeCount);

            likeBlock.className = 'like-block';

            editButton.type = 'submit';
            editButton.innerText = 'edit';
            editButton.className = 'comment-edit-button';

            editForm.append(editButton);
            // editForm.action = '/api/v1/comments/'+el[id]+'/edit';
            editForm.action = '/posts/'+el['postId']+'/comment/'+el['id']+'/edit';
            editForm.method = 'get';
            editForm.className = 'comment-edit-form';
            // editForm.addEventListener('submit', edit)

            commentActions.append(likeBlock);
            if(currentUser['id'] === authorId){
                commentActions.append(editForm);
            }
            commentActions.className = 'comment-actions';

            let commentUnit = document.createElement('div');
            commentUnit.className = 'comment-unit';
            commentUnit.append(authorWrapper);
            commentUnit.append(commentBody);
            commentUnit.append(commentActions);


            commentsWrapper.append(commentUnit);
        });

        // document.getElementById('post1').append(commentsWrapper);

        $("#"+id).append(commentsWrapper);

        return  jqXHR.responseText
    });
    return t;
}

function like(event){
    event.preventDefault();
    let splittedPath = event.target.action.split('/');
    let action = splittedPath[splittedPath.length - 1];
    let commentId = splittedPath[splittedPath.length - 2];

    let url = '/api/v1/comments/'+commentId+'/'+action;

    let response = $.get({
        url: url,
        dataType: 'json',
    }).done(function (data, textStatus, jqXHR){
        // console.log(jqXHR.responseText);
        let resp = JSON.parse(jqXHR.responseText);
        // console.log(resp);

        // console.log(event.target.nextSibling.innerText);

        if(event.target.className === 'like-form'){
            event.target.className = 'unlike-form';
            event.target.action = '/posts/'+resp['post']['id']+'/comment/'+resp['id']+'/unlike';
            event.target.firstChild.firstChild.textContent = 'unlike';
        }
        else{
            event.target.className = 'like-form';
            event.target.action = '/posts/'+resp['post']['id']+'/comment/'+resp['id']+'/like';
            event.target.firstChild.firstChild.textContent = 'like';
        }

        event.target.nextSibling.innerText = resp['likes'].length;

    });

}

function edit(event){

}

function likePost(event){
    // event.preventDefault();
    console.log(event.target)

    let splittedPath = event.target.action.split("/");
    let action = splittedPath[splittedPath.length - 1];
    let postId = splittedPath[splittedPath.length - 2];
    let url = '/api/v1/posts/'+postId+'/'+action;

    console.log(event.target.getElementsByClassName('like-button'));

    $.get({
        url: url,
        dataType: 'json',
    }).done(function (data, textStatus, jqXHR){
        let resp = JSON.parse(jqXHR.responseText)
        if(action === 'like'){

            event.target.lastElementChild.className = 'unlike-button';
            event.target.lastElementChild.innerText = 'unlike';

            // event.target.childNodes.forEach(el =>{
            //     if(el.className === 'like-button'){
            //         el.className = 'unlike-button';
            //         el.textContent = 'unlike';
            //     }
            // });
            // event.target.firstChild.textContent = 'unlike';
            // event.target.getElementsByClassName('like-button').textContent = 'unlike';
            event.target.action = '/posts/'+postId+'/unlike';
            event.target.className = 'post_unlike-form';
        }else{

            event.target.lastElementChild.className = 'like-button';
            event.target.lastElementChild.innerText = 'like';

            // event.target.childNodes.forEach(el =>{
            //     if(el.className === 'unlike-button'){
            //         el.className = 'like-button';
            //         el.textContent = 'like';
            //     }
            // });
            // event.target.firstChild.textContent = 'like';
            // event.target.getElementsByClassName('unlike-button').textContent = 'like';
            event.target.action = '/posts/'+postId+'/like';
            event.target.className = 'post_like-form';
        }

        // event.target.addEventListener('submit', likePost);
        console.log(resp['likes']);
        event.target.nextElementSibling.innerText = resp['likes'].length;
        // event.target.nextElementSibling.innerText= 10;
        // console.log(event.target.nextElementSibling);
    });
}

function createComment(event){
    event.preventDefault();
    // console.log(event.target.);
    let commentContent = event.currentTarget.firstChild.firstChild.value;
    let splittedPath = event.currentTarget.action.split('/');
    let postId = splittedPath[splittedPath.length-3];
    console.log(postId);
    let url = '/api/v1/comments/'+postId+'/create';
    $.post(
        url,
        {
            body: commentContent,
        }
    ).done(function (data, textStatus, jqHXR){
        event.target.parentNode.parentNode.parentNode.removeChild(event.target.parentNode.parentNode.parentNode.lastElementChild);
        getComments('/api/v1/comments/'+postId);
        // getComments('/api/v1/comments/'+postId ,event.target.parentNode.parentNode.parentNode.lastElementChild)
    });

}