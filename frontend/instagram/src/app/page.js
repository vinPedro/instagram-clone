"use client"

import Image from "next/image";
import Link from 'next/link';
import Form from 'next/form'

import Footer from './components/footer';


export default function Home() {

  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
      <main className="flex flex-row gap-8 row-start-2 items-center sm:items-start">
        <Image src={`/images/home-phones-2x.png`} priority={true} alt={"2 phones"} width="380" height="580" />

        <div>
          <main className="w-[350px] h-[500px] justify-items-center">
            <div className="flex flex-col border w-[100%] h-[80%] justify-items-center">
              <Image className="mx-auto" src={`/images/instagram-text-logo.png`} alt={"instagram text logo"} width="200" height="100" />
              <div className="justify-items-center w-full grow">
                <Form action="/" className="mt-10 w-64 flex flex-col gap-3 mx-auto">
                    <input className="w-full rounded border bg-gray-100 p-2 text-xs" name="email" type="text" placeholder="Entre com seu Email" />
                    <input className="w-full rounded border bg-gray-100 p-2 text-xs" name="password" type="password" placeholder="Entre com sua senha" />
                    <button type="submit" className="duration-700 hover:bg-blue-800 w-full rounded border bg-[#0095F6] p-2 text-xs text-white font-bold">Log in</button>
                </Form>
                <div className="flex space-x-2 w-64 mt-4 items-center">
                  <span className="bg-gray-300 h-px flex-1" />
                  <span className="p-2 uppercase text-xs text-gray-400 font-semibold">or</span>
                  <span className="bg-gray-300 h-px flex-1" />
                </div>
                <button className="mt-4 flex mx-auto">
                  <div className="bg-no-repeat facebook-logo mr-1"><Image src={`/images/facebook-logo.png`} alt={"facebook logo"} width="20" height="20" /></div>
                  <span>Log in with Facebook</span>
                </button>
                <button className="mt-4 flex mx-auto">
                  <span>Forgot Password</span>
                </button>
              </div>
            </div>

            <div className="flex mt-5 border w-[100%] h-[15%] items-center justify-items-center">
              <div className="mx-auto">Don't have an account? <Link className="hover:underline text-blue-600 hover:text-blue-800 visited:text-purple-600" href="/signup/">Sign up</Link></div>
            </div>
          </main>

        </div>
      </main>

      {<Footer />}
    </div>
  );
}
