import { z } from "zod";

export const customerSchema = z.object({
    id: z.string().uuid(),
    name: z.string().default("no-name"),
    surname: z.string().default("no-surname"),
    email: z.string().default("no-email"),
    address: z.string().default("no-address"),
    phoneNumber: z.string().default("no-phoneNumber"),
    company: z.string().default("no-company"),
    contactStatus: z.string().default("no-contactStatus"),

}).optional();


export type Customer = z.infer<typeof customerSchema>;