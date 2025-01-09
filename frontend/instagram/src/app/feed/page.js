import Image from "next/image";

export default function Feed() {

    return (
        <div className="grid items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
        <main className="flex flex-col gap-8 items-center">
            <h1 className="text-3xl">GOOD JOB! 👍</h1>
            <Image
                src={'https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExM3hhcHVrMXBjbGxhNmd6NGIxejB2Y3U3cmRhbjAxMXA5OTB6YzV0eCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/26FmRMFqoOcs18t9e/giphy.webp'}
                layout={'responsive'}
                height={175}
                width={175}
                alt={`friends`}
                unoptimized={true}
            />
            <h1 className="text-3xl">Você está logado.</h1>
            <h1 className="text-1xl">Aqui é era para ser o feed, quem sabe no futuro. 👷⚙️</h1>
        </main>
      </div>
    );
}
