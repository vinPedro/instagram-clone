"use client"

import Image from "next/image";
import Link from 'next/link';
import Form from 'next/form'

import Footer from '../components/footer';

export default function Signup() {

  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
        <main className="h-full flex flex-row gap-8 row-start-2 items-center sm:items-start">
            <div className="flex flex-col border w-[100%] h-[100%] justify-items-center">
                <Image className="mx-auto" src={`/images/instagram-text-logo.png`} alt={"instagram text logo"} width="200" height="100" />
                <div className="w-[80%] mt-5 mx-auto text-center">
                    Sign up to see photos and videos from your friends.
                </div>
                <div className="justify-items-center w-full grow">
                    <Form action="/" className="mt-10 w-64 flex flex-col gap-3">
                        <input className="w-full rounded border bg-gray-100 p-2 text-xs" name="email" type="text" placeholder="Entre com seu Email" />
                        <input className="w-full rounded border bg-gray-100 p-2 text-xs" name="password" type="password" placeholder="Entre com sua senha" />
                        <input className="w-full rounded border bg-gray-100 p-2 text-xs" name="fullname" type="text" placeholder="Entre com seu nome completo" />
                        <input className="w-full rounded border bg-gray-100 p-2 text-xs" name="username" type="text" placeholder="Entre com seu username" />
                        <button type="submit" className="duration-700 hover:bg-blue-800 w-full rounded border bg-[#0095F6] p-2 text-xs text-white font-bold">Sign up</button>
                    </Form>
                </div>
            </div>
        </main>

      {<Footer />}
    </div>
  );
}
