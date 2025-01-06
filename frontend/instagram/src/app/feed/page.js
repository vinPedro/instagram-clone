import Image from "next/image";

export default function Feed() {
    // Simulando dados do feed
    const posts = [
        {
            id: 1,
            username: 'john_doe',
            content: 'Hello, world! This is my first post.',
            image: 'https://embratur.com.br/wp-content/uploads/2022/07/Embratur-Brasil-ultrapassa-marca-de-1-milhao-de-turistas-estrangeiros-recebidos-pela-primeira-vez-desde-2020-1.png',
        },
        {
            id: 2,
            username: 'jane_doe',
            content: 'Just another post!',
            image: 'https://assets-cdn.123rf.com/index/static/assets/top-section-bg.jpeg',
        },
        {
            id: 3,
            username: 'mark_smith',
            content: 'Loving the new features on the platform.',
            image: 'https://d2d9keg5qvul8r.cloudfront.net/wp-content/uploads/2019/07/16110310/img-destaque-viajar-operadora-peru-magnifico-montanha-sete-cores.jpg',
        }
    ];

    return (
        <div className="feed-container">
            <h1>The Feed</h1>
            <div className="feed-posts">
                {posts.map((post) => (
                    <div key={post.id} className="feed-post">
                        <h2>{post.username}</h2>
                        <p>{post.content}</p>
                        <div className="post-image">
                            <img src={post.image} alt="Post image" width={500} height={300} />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
