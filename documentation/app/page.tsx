import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function HomePage() {
  return (
    <div className="w-full h-screen flex items-center justify-center">
      <section className="container flex flex-col items-center gap-8 pb-8 pt-6 sm:gap-10 max-w-4xl">
        <h1 className="max-w-2xl text-center font-heading text-4xl font-semibold sm:text-5xl tracking-tight">
          Worksale Dokumentation
        </h1>
        <p className="max-w-lg text-center text-lg text-muted-foreground sm:text-xl">
          Markdown basierte Dokumentation für Worksale
        </p>
        <div>
          <Button size="lg" asChild className="cursor-pointer">
            <Link href="/index">Los gehts</Link>
          </Button>
        </div>
      </section>
    </div>
  );
}