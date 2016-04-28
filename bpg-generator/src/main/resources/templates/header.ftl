<#import "/spring.ftl" as spring />
<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-fixed-top .navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<@spring.url '/'/>">BPG Web Encoder</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="<@spring.url '/show.html'/>">Display a BPG image</a></li>
                <li><a href="http://bellard.org/bpg" target="_blank">BPG Web site</a></li>
                <li><a href="<@spring.url '/support.html'/>">&copy; Promote BPG</a></li>
            </ul>
        </div>
    </div>
</div>