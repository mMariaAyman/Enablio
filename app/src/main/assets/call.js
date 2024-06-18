let localvideo = document.getElementById("local-video");
let remotevideo = document.getElementById("remote-video");

localvideo.style.opacity = 0
remotevideo.style.opacity = 0

localvideo.onplaying = ()=> {localvideo.opacity = 1}
remotevideo.onplaying = ()=> {remotevideo.opacity = 1}

let peer
function init(userId){
    peer = new Peer(userId,{
        host: '192.168.1.3',
        port: 9000 ,
        path:'/'

    }
    )

    peer.on('open', ()=>{
        // WE WILL MAKE A CALL TO KOTLIN IN ANDROID 



    })
    listen()
}
function listen(){
    peer.on('call', (call)=>{
        navigator.getUserMedia({
            audio:true,
            video:true
        }, (stream)=>{
            localvideo.srcObject = stream
            localStream = stream

            call.answer(stream)
            call.on('stream', (remoteStream)=>{
                remotevideo.srcObject = remoteStream

                remotevideo.className = "primary-video"
                localvideo.className = "secondary-video"
            })


        })
    })
}

function startCall(otherUSerId){
    navigator.getUserMedia({
        audio:true,
        video:true
    }, (stream)=>{
        localvideo.srcObject = stream
        localStream = stream
        const call = peer.call(otherUSerId, stream)
        call.on('stream', (remoteStream)=>{
            remotevideo.srcObject = remoteStream

            remotevideo.className = "primary-video"
            localvideo.className = "secondary-video"
        })
    })

}

function toggleVideo(b){
    if(b=="true"){
        localStream.getVideoTracks()[0].enabled = true
    } else{
        localStream.getVideoTracks()[0].enabled = false
    }
}

function toggleAudio(b){
    if(b=="true"){
        localStream.getAudioTracks()[0].enabled = true
    } else{
        localStream.getAudioTracks()[0].enabled = false
    }
}